package com.example.mythos.ui.screens.profile.mynovels.novelform

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.NovelFormDto
import com.example.mythos.data.managers.AccountManager
import com.example.mythos.data.managers.NovelManager
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NovelFormViewModel : ViewModel() {

    private val novelRepository = NovelRepository()

    // Estados principales
    private val _uiState = MutableStateFlow(NovelFormUiState())
    val uiState = _uiState.asStateFlow()

    // Datos estáticos
    private val availableGenres = listOf(
        "Acción", "Aventura", "Romance", "Terror", "Drama", "Fantasía", "Fantasía oscura", "Ciencia ficción",
        "Comedia", "Misterio", "Misterio sobrenatural", "Cultivo", "Superheroes"
    )

    private val availableTags = listOf(
        "Horror cósmico", "Ocultismo", "Sistema de pociones", "Protagonista inteligente", "Sociedades secretas",
        "Mundo detallado", "Conspiraciones", "Locura", "Terror psicológico", "Progresión de poder", "Dimensiones",
        "Sistema genético", "Cultivo moderno", "Torneos", "Tecnología avanzada", "Batallas épicas", "Mundo cruel",
        "Ascenso imparable", "Bucle temporal", "Anti héroe", "Superpoderes", "Optimización", "Distopía",
        "Narrativa tipo juego", "Humor negro", "Protagonista sarcastico", "Megacorporaciones"
    )

    // Propiedades derivadas
    val availableGenresForSelection = _uiState.map { state ->
        availableGenres.filter { it !in state.novelForm.genres }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val availableTagsForSelection = _uiState.map { state ->
        availableTags.filter { it !in state.novelForm.tags }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val isFormValid = _uiState.map { state ->
        state.novelForm.title.isNotBlank() &&
                state.novelForm.description.isNotBlank() &&
                state.novelForm.genres.isNotEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    val isEditingMode = _uiState.map { state ->
        state.novelForm.id != null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    init {
        initializeForm()
    }

    private fun initializeForm() {
        val currentUser = AccountManager.getCurrentUserId()?.toString() ?: ""
        val currentUserName = AccountManager.getCurrentUserName() ?: "Usuario"

        val novelToEdit = NovelManager.currentNovelForEditing

        val initialForm = novelToEdit?.copy(
            writerAccountId = currentUser,
            writerName = currentUserName
        ) ?: createEmptyForm(currentUser, currentUserName)

        _uiState.update {
            it.copy(novelForm = initialForm)
        }
    }

    private fun createEmptyForm(userId: String, userName: String) = NovelFormDto(
        id = null,
        writerAccountId = userId,
        writerName = userName,
        title = "",
        description = "",
        genres = emptyList(),
        tags = emptyList(),
        views = 0,
        isPublic = true,
        coverImageUrl = "",
        status = "En curso",
        createdAt = null,
        updatedAt = null
    )

    // Acciones de la UI
    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(novelForm = it.novelForm.copy(title = title))
        }
    }

    fun updateDescription(description: String) {
        _uiState.update {
            it.copy(novelForm = it.novelForm.copy(description = description))
        }
    }

    fun updateSelectedImageUri(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun addGenre(genre: String) {
        _uiState.update { state ->
            val updatedGenres = (state.novelForm.genres + genre).distinct()
            state.copy(novelForm = state.novelForm.copy(genres = updatedGenres))
        }
    }

    fun removeGenre(genre: String) {
        _uiState.update { state ->
            val updatedGenres = state.novelForm.genres.filter { it != genre }
            state.copy(novelForm = state.novelForm.copy(genres = updatedGenres))
        }
    }

    fun addTag(tag: String) {
        _uiState.update { state ->
            val updatedTags = (state.novelForm.tags + tag).distinct()
            state.copy(novelForm = state.novelForm.copy(tags = updatedTags))
        }
    }

    fun removeTag(tag: String) {
        _uiState.update { state ->
            val updatedTags = state.novelForm.tags.filter { it != tag }
            state.copy(novelForm = state.novelForm.copy(tags = updatedTags))
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun saveNovel(context: Context, onSuccess: () -> Unit) {
        if (_uiState.value.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                val currentState = _uiState.value
                val coverImageUrl = uploadImageIfNeeded(context, currentState)

                val novelToSave = currentState.novelForm.copy(
                    coverImageUrl = coverImageUrl
                )

                if (novelToSave.id == null) {
                    novelRepository.createNovel(novelToSave)
                } else {
                    novelRepository.updateNovel(novelToSave.id.toString(), novelToSave)
                }

                // Limpiar imagen seleccionada después del éxito
                _uiState.update { it.copy(selectedImageUri = null) }
                onSuccess()

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = e.message ?: "Error desconocido")
                }
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    private suspend fun uploadImageIfNeeded(context: Context, state: NovelFormUiState): String {
        return when {
            state.selectedImageUri != null -> {
                val imageBytes = readBytesFromUri(context, state.selectedImageUri)
                novelRepository.uploadCoverImage(
                    coverImageBytes = imageBytes,
                    fileName = "cover_${System.currentTimeMillis()}.jpg"
                ).trim('"')
            }
            else -> state.novelForm.coverImageUrl.trim('"')
        }
    }

    private fun readBytesFromUri(context: Context, uri: Uri): ByteArray {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            inputStream.readBytes()
        } ?: throw IllegalArgumentException("No se pudo abrir el URI: $uri")
    }

    fun resetForm() {
        val currentUser = AccountManager.getCurrentUserId()?.toString() ?: ""
        val currentUserName = AccountManager.getCurrentUserName() ?: "Usuario"

        _uiState.update {
            NovelFormUiState(
                novelForm = createEmptyForm(currentUser, currentUserName)
            )
        }
    }
}

// Estado consolidado de la UI
data class NovelFormUiState(
    val novelForm: NovelFormDto = NovelFormDto(
        id = null,
        writerAccountId = "",
        writerName = "",
        title = "",
        description = "",
        genres = emptyList(),
        tags = emptyList(),
        views = 0,
        isPublic = true,
        coverImageUrl = "",
        status = "En curso",
        createdAt = null,
        updatedAt = null
    ),
    val selectedImageUri: Uri? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)