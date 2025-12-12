package com.example.mobileappdevelopment.veiwmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileappdevelopment.api.LoginRequest
import com.example.mobileappdevelopment.api.RetrofitClient
import com.example.mobileappdevelopment.api.TokenManager
import com.example.mobileappdevelopment.data.User
import com.example.mobileappdevelopment.data.UserRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    fun login(email: String, password: String, role: UserRole) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.apiService.login(request)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loginResponse?.let {
                        // Save token
                        TokenManager.saveToken(it.token)

                        // Create User object
                        val userRole = if (it.role == "ADMIN") UserRole.ADMIN else UserRole.EMPLOYEE
                        _currentUser.value = User(
                            email = email,
                            name = it.name,
                            role = userRole
                        )
                        _loginError.value = null
                    }
                } else {
                    _loginError.value = "Login failed."
                }
            } catch (e: Exception) {
                _loginError.value = "Network error: ${e.message}"
            }
//
//            /*Test ID & Password*/
//            when {
//                //Admin
//                email == "admin@company.com" && password == "admin123" && role == UserRole.ADMIN -> {
//                    _currentUser.value = User(
//                        email = "admin@company.com",
//                        name = "Admin",
//                        role = UserRole.ADMIN,
//                        department = "System Management" // Add if department field exists
//                    )
//                    _loginError.value = null
//                }
//                //Employee
//                email == "minsu.kim@company.com" && password == "password123" && role == UserRole.EMPLOYEE -> {
//                    _currentUser.value = User(
//                        email = "minsu.kim@company.com",
//                        name = "Minsu Kim",
//                        role = UserRole.EMPLOYEE,
//                        department = "Development Team" // Add if department field exists
//                    )
//                    _loginError.value = null
//                }
//
//                else -> {
//                    _loginError.value = "Incorrect email or password."
//                }
//            }
        }
    }

    fun logout() {
        TokenManager.clearToken()  // Clear token
        _currentUser.value = null
    }
}