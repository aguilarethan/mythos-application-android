package com.example.mythos.ui.screens.becomewriter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BecomeWriterScreen(
    viewModel: BecomeWriterViewModel,
    onBackToProfile: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val name by viewModel.name.collectAsStateWithLifecycle()
    val lastName by viewModel.lastName.collectAsStateWithLifecycle()
    val birthDate by viewModel.birthDate.collectAsStateWithLifecycle()
    val birthDateText by viewModel.birthDateText.collectAsStateWithLifecycle()
    val country by viewModel.country.collectAsStateWithLifecycle()
    val biography by viewModel.biography.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val isFormValid by viewModel.isFormValid.collectAsStateWithLifecycle()
    val isSuccess by viewModel.isSuccess.collectAsStateWithLifecycle()

    // Manejar el éxito
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            onSuccess()
            viewModel.resetSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Título simple y limpio
        Text(
            text = "¡Conviértete en Escritor!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Completa tu perfil para comenzar a escribir y publicar novelas",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Mostrar error si existe
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = { viewModel.clearError() }
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }

        // Formulario sin card para mejor legibilidad
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Nombre
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Nombre") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !viewModel.isNameValid() && name.isNotEmpty(),
                supportingText = {
                    viewModel.getNameError()?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Apellido
            OutlinedTextField(
                value = lastName,
                onValueChange = { viewModel.updateLastName(it) },
                label = { Text("Apellido") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !viewModel.isLastNameValid() && lastName.isNotEmpty(),
                supportingText = {
                    viewModel.getLastNameError()?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha de nacimiento
            OutlinedTextField(
                value = birthDateText,
                onValueChange = { newValue ->
                    // Solo permitir números y guiones
                    if (newValue.all { it.isDigit() || it == '-' }) {
                        viewModel.updateBirthDateText(newValue)
                    }
                },
                label = { Text("Fecha de Nacimiento (YYYY-MM-DD)") },
                leadingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("1990-01-01") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = !viewModel.isBirthDateValid(),
                supportingText = {
                    viewModel.getBirthDateError()?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // País
            OutlinedTextField(
                value = country,
                onValueChange = { viewModel.updateCountry(it) },
                label = { Text("País") },
                leadingIcon = {
                    Icon(Icons.Default.Public, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = !viewModel.isCountryValid() && country.isNotEmpty(),
                supportingText = {
                    viewModel.getCountryError()?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Biografía
            OutlinedTextField(
                value = biography,
                onValueChange = { viewModel.updateBiography(it) },
                label = { Text("Biografía") },
                leadingIcon = {
                    Icon(Icons.Default.Description, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 6,
                placeholder = { Text("Cuéntanos sobre ti, tu experiencia escribiendo, tus intereses...") },
                isError = !viewModel.isBiographyValid() && biography.isNotEmpty(),
                supportingText = {
                    viewModel.getBiographyError()?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de envío
        Button(
            onClick = { viewModel.becomeWriter() },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Procesando...")
            } else {
                Text(
                    text = "Convertirse en Escritor",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto informativo
        Text(
            text = "Al convertirte en escritor, podrás crear y publicar novelas en la plataforma. Tu perfil será visible para otros usuarios.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}


// Composable auxiliar para el selector de fecha (implementación simple)
@Composable
fun DatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = selectedDate?.toString() ?: "",
        onValueChange = { dateString ->
            try {
                val date = LocalDate.parse(dateString)
                onDateSelected(date)
            } catch (e: Exception) {
                // Manejar error de parsing
            }
        },
        label = { Text("Fecha de Nacimiento") },
        leadingIcon = {
            Icon(Icons.Default.CalendarToday, contentDescription = null)
        },
        modifier = modifier,
        singleLine = true,
        placeholder = { Text("YYYY-MM-DD") }
    )
}