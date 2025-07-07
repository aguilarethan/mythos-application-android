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

    private val _novelForm = MutableStateFlow(
        NovelFormDto(
            id = null,
            writerAccountId = AccountManager.getCurrentUserId().toString(),
            writerName = AccountManager.getCurrentUserName().toString(), // Se establecerá desde AccountManager
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

    private val _availableGenres = MutableStateFlow(
        listOf(
            "Acción", "Aventura", "Romance", "Fantasía", "Ciencia Ficción",
            "Misterio", "Terror", "Drama", "Comedia", "Histórico",
            "Thriller", "Sobrenatural", "Slice of Life", "Psicológico"
        )
    )
    val availableGenres = _availableGenres.asStateFlow()

    // Lista de etiquetas predefinidas
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
        _isLoading.update { true }

        return try {
            if (!isFormValid()) throw Exception("Por favor completa todos los campos requeridos.")

            val imageUri = _selectedImageUri.value

            val coverImageUrl = if (imageUri != null) {
                val imageBytes = readBytesFromUri(context, imageUri)

                novelRepository.uploadCoverImage(
                    coverImageBytes = imageBytes,
                    fileName = "cover_${System.currentTimeMillis()}.jpg"
                ).trim('"')
            } else {
                _novelForm.value.coverImageUrl.trim('"')
            }

            val novelToSave = _novelForm.value.copy(
                coverImageUrl = coverImageUrl
            )

            if (novelToSave.id == null) {
                novelRepository.createNovel(novelToSave)
            } else {
                novelRepository.updateNovel(novelToSave.id.toString(), novelToSave)
            }

            true
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Error desconocido"
            false
        } finally {
            _isLoading.update { false }
        }
    }



    fun isFormValid(): Boolean {
        val form = _novelForm.value
        return form.title.isNotBlank() &&
                form.description.isNotBlank() &&
                form.genres.isNotEmpty()
    }
}