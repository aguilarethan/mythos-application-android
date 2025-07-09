package com.example.mythos.ui.screens.profile.mynovels.novelform

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.mythos.data.dtos.NovelFormDto
import com.example.mythos.data.managers.AccountManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NovelFormViewModel : ViewModel() {

    private val novelRepository = NovelRepository()

    fun initializeForEditing(novel: NovelFormDto) {
        _novelForm.update {
            novel.copy(
                writerAccountId = AccountManager.getCurrentUserId().toString(),
                writerName = AccountManager.getCurrentUserName().toString()
            )
        }
    }

    private val _novelForm = MutableStateFlow(
        NovelFormDto(
            id = null,
            writerAccountId = AccountManager.getCurrentUserId().toString(),
            writerName = AccountManager.getCurrentUserName().toString(),
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
    )
    val novelForm = _novelForm.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri = _selectedImageUri.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Flag para evitar múltiples envíos
    private var isSaving = false

    private val _availableGenres = MutableStateFlow(
        listOf(
            "Acción", "Aventura", "Romance", "Fantasía", "Ciencia Ficción",
            "Misterio", "Terror", "Drama", "Comedia", "Histórico",
            "Thriller", "Sobrenatural", "Slice of Life", "Psicológico"
        )
    )
    val availableGenres = _availableGenres.asStateFlow()

    private val _availableTags = MutableStateFlow(
        listOf(
            "Protagonista fuerte", "Magia", "Sistema", "Reencarnación",
            "Mundo alternativo", "Poderes", "Escuela", "Trabajo",
            "Familia", "Amistad", "Venganza", "Supervivencia"
        )
    )
    val availableTags = _availableTags.asStateFlow()

    fun loadNovelForEditing(novel: NovelFormDto) {
        _novelForm.update { novel }
    }

    fun updateTitle(title: String) {
        _novelForm.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _novelForm.update { it.copy(description = description) }
    }

    fun initializeWithUserName(userName: String) {
        _novelForm.update { it.copy(writerName = userName) }
    }

    fun addGenre(genre: String) {
        _novelForm.update {
            it.copy(genres = (it.genres + genre).distinct())
        }
    }

    fun removeGenre(genre: String) {
        _novelForm.update {
            it.copy(genres = it.genres.filter { g -> g != genre })
        }
    }

    fun addTag(tag: String) {
        _novelForm.update {
            it.copy(tags = (it.tags + tag).distinct())
        }
    }

    fun removeTag(tag: String) {
        _novelForm.update {
            it.copy(tags = it.tags.filter { t -> t != tag })
        }
    }

    fun updateSelectedImageUri(uri: Uri?) {
        _selectedImageUri.update { uri }
    }

    fun readBytesFromUri(context: Context, uri: Uri): ByteArray {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            return inputStream.readBytes()
        } ?: throw IllegalArgumentException("No se pudo abrir el URI: $uri")
    }

    suspend fun saveNovel(context: Context): Boolean {
        // Prevenir múltiples envíos simultáneos
        if (isSaving) {
            return false
        }

        isSaving = true
        _isLoading.update { true }
        _errorMessage.update { null } // Limpiar errores previos

        return try {
            if (!isFormValid()) {
                throw Exception("Por favor completa todos los campos requeridos.")
            }

            val currentForm = _novelForm.value
            val imageUri = _selectedImageUri.value

            // Subir imagen solo si hay una nueva seleccionada
            val coverImageUrl = if (imageUri != null) {
                val imageBytes = readBytesFromUri(context, imageUri)
                novelRepository.uploadCoverImage(
                    coverImageBytes = imageBytes,
                    fileName = "cover_${System.currentTimeMillis()}.jpg"
                ).trim('"')
            } else {
                currentForm.coverImageUrl.trim('"')
            }

            val novelToSave = currentForm.copy(
                coverImageUrl = coverImageUrl
            )

            // Ejecutar una sola operación según el caso
            val result = if (novelToSave.id == null) {
                // Crear nueva novela
                novelRepository.createNovel(novelToSave)
            } else {
                // Actualizar novela existente
                novelRepository.updateNovel(novelToSave.id.toString(), novelToSave)
            }

            // Limpiar la imagen seleccionada después de guardar exitosamente
            _selectedImageUri.update { null }

            true
        } catch (e: Exception) {
            _errorMessage.update { e.message ?: "Error desconocido" }
            false
        } finally {
            _isLoading.update { false }
            isSaving = false // Liberar el flag
        }
    }

    fun isFormValid(): Boolean {
        val form = _novelForm.value
        return form.title.isNotBlank() &&
                form.description.isNotBlank() &&
                form.genres.isNotEmpty()
    }

    // Método para limpiar errores manualmente si es necesario
    fun clearError() {
        _errorMessage.update { null }
    }

    // Método para resetear el formulario
    fun resetForm() {
        _novelForm.update {
            NovelFormDto(
                id = null,
                writerAccountId = AccountManager.getCurrentUserId().toString(),
                writerName = AccountManager.getCurrentUserName().toString(),
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
        }
        _selectedImageUri.update { null }
        _errorMessage.update { null }
    }
}