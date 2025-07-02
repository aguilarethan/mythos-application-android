package com.example.mythos.ui.screens.chapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.ChapterDto
import com.example.mythos.data.repositories.ChapterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChapterViewModel : ViewModel() {

    private val repository = ChapterRepository()

    private val _chapter = MutableStateFlow<ChapterDto?>(null)
    val chapter: StateFlow<ChapterDto?> = _chapter

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadChapter(chapterId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = repository.getChapterById(chapterId)
                _chapter.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}