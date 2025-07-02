package com.example.mythos.ui.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mythos.ui.components.PasswordTextField


@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    modifier: Modifier = Modifier,
    onRegisterSuccess: () -> Unit = {},
    onBackToLogin: () -> Unit
) {
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Regístrate",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Crea una cuenta nueva",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = viewModel::onUsernameChanged,
            label = { Text("Nombre de usuario") },
            placeholder = { Text("mythos") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChanged,
            label = { Text("Correo electrónico") },
            placeholder = { Text("usuario@correo.com") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        PasswordTextField(
            value = password,
            onValueChange = viewModel::onPasswordChanged,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Button(
            onClick = { viewModel.register(onRegisterSuccess) },
            enabled = username.isNotBlank() && email.isNotBlank() && password.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}