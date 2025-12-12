package com.example.mobileappdevelopment.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.mobileappdevelopment.data.UserRole

@Composable
fun LoginScreen(
    onLogin: (String, String, UserRole) -> Unit,
    error: String?
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.EMPLOYEE) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Whistle Box",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Log in to access the system",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Login Type",
                        style = MaterialTheme.typography.labelMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedRole == UserRole.EMPLOYEE,
                            onClick = { selectedRole = UserRole.EMPLOYEE },
                            label = { Text("Employee") },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = selectedRole == UserRole.ADMIN,
                            onClick = { selectedRole = UserRole.ADMIN },
                            label = { Text("Admin") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        placeholder = { Text("your.email@company.com") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    if (error != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.padding(12.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    Button(
                        onClick = { onLogin(email, password, selectedRole) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Login,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Login")
                    }

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Test Accounts:",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = "Admin: admin@company.com / admin123",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Employee: minsu.kim@company.com / password123",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}