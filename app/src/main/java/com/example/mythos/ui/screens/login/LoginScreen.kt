package com.example.mythos.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit = {},
    onBackToRegister: () -> Unit = {}

) {
    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Inicia sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Ingresa tu nombre de usuario y contraseña",
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

        PasswordTextField(
            value = password,
            onValueChange = viewModel::onPasswordChanged,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )

        Button(
            onClick = { viewModel.login(onLoginSuccess) },
            enabled = username.isNotBlank() && password.isNotBlank() && !isLoading,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }

        TextButton(
            onClick = onBackToRegister,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}