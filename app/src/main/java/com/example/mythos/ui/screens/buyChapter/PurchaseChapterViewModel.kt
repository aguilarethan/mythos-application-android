package com.example.mythos.ui.screens.buyChapter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.ChapterDto
import com.example.mythos.data.repositories.ChapterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PurchaseChapterViewModel : ViewModel() {
    private val repository = ChapterRepository()

    private val _chapter = MutableStateFlow<ChapterDto?>(null)
    val chapter: StateFlow<ChapterDto?> = _chapter

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing

    public val _purchaseMessage = MutableStateFlow<String?>(null)
    val purchaseMessage: StateFlow<String?> = _purchaseMessage

    private val _purchaseSuccess = MutableStateFlow(false)
    val purchaseSuccess: StateFlow<Boolean> = _purchaseSuccess

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

    fun purchaseChapter(writerId: String) {
        val chapterId = chapter.value?.id ?: return
        val price = chapter.value?.priceMythras ?: return

        viewModelScope.launch {
            _isProcessing.value = true
            _purchaseMessage.value = null
            _purchaseSuccess.value = false

            try {
                val result = repository.purchaseChapterAsync(chapterId, writerId, price)
                _purchaseMessage.value = result.message
                _purchaseSuccess.value = result.success
            } catch (e: Exception) {
                _purchaseMessage.value = "Error: ${e.message}"
            } finally {
                _isProcessing.value = false
            }
        }
    }
}