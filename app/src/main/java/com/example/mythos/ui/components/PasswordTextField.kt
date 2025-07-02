package com.example.mythos.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.mythos.ui.theme.MythosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Contraseña",
    placeholder: String = "Ingrese su contraseña"
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                Icons.Default.Lock,
                contentDescription = "Contraseña"
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar contraseña"
                    else "Mostrar contraseña"
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordTextFieldPreview() {
    MythosTheme {
        PasswordTextField(
            value = "",
            onValueChange = {}
        )
    }
}