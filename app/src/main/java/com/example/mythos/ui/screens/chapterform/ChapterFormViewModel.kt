package com.example.mythos.ui.screens.chapterform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.ChapterFormDto
import com.example.mythos.data.managers.ChapterManager
import com.example.mythos.data.repositories.ChapterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChapterFormViewModel : ViewModel() {

    private val chapterRepository = ChapterRepository()

    private val _chapterForm = MutableStateFlow(
        ChapterFormDto(
            novelId = ChapterManager.currentNovelIdForChapter.toString()
        )
    )
    val chapterForm: StateFlow<ChapterFormDto> = _chapterForm

    fun onTitleChange(newTitle: String) {
        _chapterForm.update { it.copy(title = newTitle) }
    }

    fun onContentChange(newContent: String) {
        _chapterForm.update { it.copy(content = newContent) }
    }

    fun onPriceChange(newPrice: Int) {
        _chapterForm.update { it.copy(priceMythras = newPrice) }
    }

    fun saveChapter(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                chapterRepository.saveChapter(_chapterForm.value)
                // Limpiar campos después de guardar
                clearForm()
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                // Aún así llamamos onSuccess para mantener el comportamiento original
                onSuccess()
            }
        }
    }

    private fun clearForm() {
        _chapterForm.update {
            ChapterFormDto(
                novelId = ChapterManager.currentNovelIdForChapter.toString()
            )
        }
    }
}