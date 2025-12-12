package com.example.mobileappdevelopment.veiwmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappdevelopment.api.MerkleRegisterRequest
import com.example.mobileappdevelopment.api.RetrofitClient
import com.example.mobileappdevelopment.util.ZkKeyManager
import com.loopring.poseidon.PoseidonHash
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger

class ZkViewModel(application: Application) : AndroidViewModel(application) {

    private val _registrationStatus = MutableStateFlow<String?>(null)
    val registrationStatus: StateFlow<String?> = _registrationStatus.asStateFlow()

    fun registerZkKeys(customNullifier: String, secret: String) {
        viewModelScope.launch {
            try {
                val leaf = withContext(Dispatchers.Default) {
                    try {
                        val hasher: PoseidonHash = PoseidonHash.Digest.newInstance(PoseidonHash.DefaultParams)
                        // The library is strict by default, so we disable it to prevent crashes if hex strings are not field elements.
                        (hasher as PoseidonHash.Digest).setStrict(false)

                        hasher.add(BigInteger(customNullifier, 16))
                        hasher.add(BigInteger(secret, 16))
                        
                        // digest(false) returns BigInteger[] where the first element is the hash
                        hasher.digest(false)[0].toString(16)
                    } catch (e: NumberFormatException) {
                        _registrationStatus.value = "Input must contain only hexadecimal characters (0-9, a-f)."
                        null
                    }
                }

                if (leaf == null) return@launch

                val request = MerkleRegisterRequest(leaf)
                val response = RetrofitClient.apiService.registerMerkle(request)

                if (response.isSuccessful) {
                    ZkKeyManager.saveKeys(getApplication(), customNullifier, secret)
                    _registrationStatus.value = "Successfully registered."
                } else {
                    _registrationStatus.value = "Registration failed: ${response.message()}"
                }
            } catch (e: Exception) {
                _registrationStatus.value = "An error occurred: ${e.message}"
            }
        }
    }
}
