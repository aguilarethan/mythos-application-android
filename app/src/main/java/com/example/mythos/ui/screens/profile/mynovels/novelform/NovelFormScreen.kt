package com.example.mythos.ui.screens.profile.mynovels.novelform

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mythos.data.dtos.NovelFormDto
import com.example.mythos.data.managers.AccountManager
import com.example.mythos.data.managers.NovelManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelFormScreen(
    viewModel: NovelFormViewModel,
    novel: NovelFormDto? = null,
    onSaveSuccess: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val novelForm by viewModel.novelForm.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val availableGenres by viewModel.availableGenres.collectAsState()
    val availableTags by viewModel.availableTags.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isSaveInProgress by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        NovelManager.currentNovelForEditing?.let { novel ->
            viewModel.initializeForEditing(novel)
        }
    }

    // Limpiar el manager cuando se destruya el composable
    DisposableEffect(Unit) {
        onDispose {
            NovelManager.clearNovelForEditing()
        }
    }

    // Mostrar mensaje de error si existe
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            // Aquí puedes mostrar un Toast o Snackbar
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.updateSelectedImageUri(uri)
    }

    // Estados para chips de géneros y etiquetas
    var showGenreDropdown by remember { mutableStateOf(false) }
    var showTagDropdown by remember { mutableStateOf(false) }

    // Inicializar con username actual
    LaunchedEffect(Unit) {
        val userName = AccountManager.getCurrentUserName() ?: "Usuario"
        viewModel.initializeWithUserName(userName)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header similar a NovelScreen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de portada
            Card(
                modifier = Modifier
                    .size(width = 120.dp, height = 180.dp)
                    .clip(MaterialTheme.shapes.medium),
                onClick = {
                    if (!isLoading) {
                        imagePickerLauncher.launch("image/*")
                    }
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        selectedImageUri != null -> {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(selectedImageUri)
                                    .build(),
                                contentDescription = "Portada seleccionada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        novelForm.coverImageUrl.isNotEmpty() -> {
                            AsyncImage(
                                model = novelForm.coverImageUrl,
                                contentDescription = "Portada actual",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Agregar portada",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    "Toca para agregar portada",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Información de la novela (solo visualización)
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (novelForm.title.isNotBlank()) novelForm.title else "Título de la novela",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Autor: ${novelForm.writerName}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Estado: ${novelForm.status}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Vistas: ${novelForm.views}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = novelForm.title,
            onValueChange = viewModel::updateTitle,
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = novelForm.description,
            onValueChange = viewModel::updateDescription,
            label = { Text("Descripción") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Géneros",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // Géneros seleccionados
        if (novelForm.genres.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                novelForm.genres.forEach { genre ->
                    FilterChip(
                        onClick = {
                            if (!isLoading) {
                                viewModel.removeGenre(genre)
                            }
                        },
                        label = { Text(genre) },
                        selected = true,
                        enabled = !isLoading,
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Eliminar",
                                modifier = Modifier.size(16.dp),
                            )
                        }
                    )
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = showGenreDropdown,
            onExpandedChange = {
                if (!isLoading) {
                    showGenreDropdown = !showGenreDropdown
                }
            }
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                readOnly = true,
                label = { Text("Agregar género") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showGenreDropdown) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = !isLoading
            )

            ExposedDropdownMenu(
                expanded = showGenreDropdown,
                onDismissRequest = { showGenreDropdown = false }
            ) {
                availableGenres.filter { it !in novelForm.genres }.forEach { genre ->
                    DropdownMenuItem(
                        text = { Text(genre) },
                        onClick = {
                            viewModel.addGenre(genre)
                            showGenreDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Etiquetas
        Text(
            "Etiquetas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // Etiquetas seleccionadas
        if (novelForm.tags.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                novelForm.tags.forEach { tag ->
                    FilterChip(
                        onClick = {
                            if (!isLoading) {
                                viewModel.removeTag(tag)
                            }
                        },
                        label = { Text(tag) },
                        selected = true,
                        enabled = !isLoading,
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Eliminar",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }
            }
        }

        // Agregar etiquetas
        ExposedDropdownMenuBox(
            expanded = showTagDropdown,
            onExpandedChange = {
                if (!isLoading) {
                    showTagDropdown = !showTagDropdown
                }
            }
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                readOnly = true,
                label = { Text("Agregar etiqueta") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showTagDropdown) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                enabled = !isLoading
            )

            ExposedDropdownMenu(
                expanded = showTagDropdown,
                onDismissRequest = { showTagDropdown = false }
            ) {
                availableTags.filter { it !in novelForm.tags }.forEach { tag ->
                    DropdownMenuItem(
                        text = { Text(tag) },
                        onClick = {
                            viewModel.addTag(tag)
                            showTagDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = {
                    if (!isSaveInProgress && !isLoading) {
                        isSaveInProgress = true
                        scope.launch {
                            try {
                                if (viewModel.saveNovel(context)) {
                                    onSaveSuccess()
                                }
                            } finally {
                                isSaveInProgress = false
                            }
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = viewModel.isFormValid() && !isLoading && !isSaveInProgress
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(if (novel != null) "Actualizar" else "Crear")
                }
            }
        }
    }
}