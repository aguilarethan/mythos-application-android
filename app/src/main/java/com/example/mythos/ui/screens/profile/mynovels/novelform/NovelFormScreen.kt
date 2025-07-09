package com.example.mythos.ui.screens.profile.mynovels.novelform

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mythos.data.managers.NovelManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovelFormScreen(
    viewModel: NovelFormViewModel,
    onSaveSuccess: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val availableGenres by viewModel.availableGenresForSelection.collectAsState()
    val availableTags by viewModel.availableTagsForSelection.collectAsState()
    val isFormValid by viewModel.isFormValid.collectAsState()
    val isEditingMode by viewModel.isEditingMode.collectAsState()

    val context = LocalContext.current

    var showGenreDropdown by remember { mutableStateOf(false) }
    var showTagDropdown by remember { mutableStateOf(false) }

    // Limpiar el manager cuando se destruya el composable
    DisposableEffect(Unit) {
        onDispose {
            NovelManager.clearNovelForEditing()
        }
    }

    // Mostrar errores
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Launcher para seleccionar imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.updateSelectedImageUri(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header con imagen de portada
        NovelCoverSection(
            selectedImageUri = uiState.selectedImageUri,
            currentCoverUrl = uiState.novelForm.coverImageUrl,
            novelForm = uiState.novelForm,
            isLoading = uiState.isSaving,
            onImageClick = {
                if (!uiState.isSaving) {
                    imagePickerLauncher.launch("image/*")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campos del formulario
        NovelFormFields(
            novelForm = uiState.novelForm,
            isLoading = uiState.isSaving,
            onTitleChange = viewModel::updateTitle,
            onDescriptionChange = viewModel::updateDescription
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de géneros
        GenreSection(
            selectedGenres = uiState.novelForm.genres,
            availableGenres = availableGenres,
            showDropdown = showGenreDropdown,
            onShowDropdownChange = { if (!uiState.isSaving) showGenreDropdown = it },
            onAddGenre = { genre ->
                viewModel.addGenre(genre)
                showGenreDropdown = false
            },
            onRemoveGenre = viewModel::removeGenre,
            isLoading = uiState.isSaving
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de etiquetas
        TagSection(
            selectedTags = uiState.novelForm.tags,
            availableTags = availableTags,
            showDropdown = showTagDropdown,
            onShowDropdownChange = { if (!uiState.isSaving) showTagDropdown = it },
            onAddTag = { tag ->
                viewModel.addTag(tag)
                showTagDropdown = false
            },
            onRemoveTag = viewModel::removeTag,
            isLoading = uiState.isSaving
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botones de acción
        ActionButtons(
            onCancel = onCancel,
            onSave = { viewModel.saveNovel(context, onSaveSuccess) },
            isLoading = uiState.isSaving,
            isFormValid = isFormValid,
            isEditingMode = isEditingMode
        )
    }
}

@Composable
private fun NovelCoverSection(
    selectedImageUri: android.net.Uri?,
    currentCoverUrl: String,
    novelForm: com.example.mythos.data.dtos.NovelFormDto,
    isLoading: Boolean,
    onImageClick: () -> Unit
) {
    val context = LocalContext.current

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
            onClick = onImageClick
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
                    currentCoverUrl.isNotEmpty() -> {
                        AsyncImage(
                            model = currentCoverUrl,
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

        // Información de la novela
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
}

@Composable
private fun NovelFormFields(
    novelForm: com.example.mythos.data.dtos.NovelFormDto,
    isLoading: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit
) {
    OutlinedTextField(
        value = novelForm.title,
        onValueChange = onTitleChange,
        label = { Text("Título") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        enabled = !isLoading
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = novelForm.description,
        onValueChange = onDescriptionChange,
        label = { Text("Descripción") },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        maxLines = 5,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        enabled = !isLoading
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenreSection(
    selectedGenres: List<String>,
    availableGenres: List<String>,
    showDropdown: Boolean,
    onShowDropdownChange: (Boolean) -> Unit,
    onAddGenre: (String) -> Unit,
    onRemoveGenre: (String) -> Unit,
    isLoading: Boolean
) {
    Text(
        "Géneros",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    // Géneros seleccionados
    if (selectedGenres.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedGenres.forEach { genre ->
                FilterChip(
                    onClick = { if (!isLoading) onRemoveGenre(genre) },
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

    // Dropdown para agregar géneros
    ExposedDropdownMenuBox(
        expanded = showDropdown,
        onExpandedChange = onShowDropdownChange
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            readOnly = true,
            label = { Text("Agregar género") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = !isLoading
        )

        ExposedDropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { onShowDropdownChange(false) }
        ) {
            availableGenres.forEach { genre ->
                DropdownMenuItem(
                    text = { Text(genre) },
                    onClick = { onAddGenre(genre) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TagSection(
    selectedTags: List<String>,
    availableTags: List<String>,
    showDropdown: Boolean,
    onShowDropdownChange: (Boolean) -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    isLoading: Boolean
) {
    Text(
        "Etiquetas",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )

    // Etiquetas seleccionadas
    if (selectedTags.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            selectedTags.forEach { tag ->
                FilterChip(
                    onClick = { if (!isLoading) onRemoveTag(tag) },
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

    // Dropdown para agregar etiquetas
    ExposedDropdownMenuBox(
        expanded = showDropdown,
        onExpandedChange = onShowDropdownChange
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { },
            readOnly = true,
            label = { Text("Agregar etiqueta") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDropdown) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            enabled = !isLoading
        )

        ExposedDropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { onShowDropdownChange(false) }
        ) {
            availableTags.forEach { tag ->
                DropdownMenuItem(
                    text = { Text(tag) },
                    onClick = { onAddTag(tag) }
                )
            }
        }
    }
}

@Composable
private fun ActionButtons(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean,
    isFormValid: Boolean,
    isEditingMode: Boolean
) {
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
            onClick = onSave,
            modifier = Modifier.weight(1f),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (isEditingMode) "Actualizar" else "Crear")
            }
        }
    }
}