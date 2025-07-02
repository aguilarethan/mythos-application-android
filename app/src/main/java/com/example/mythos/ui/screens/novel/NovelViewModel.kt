package com.example.mythos.ui.screens.novel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.ChapterDto
import com.example.mythos.data.dtos.NovelDetailDto
import com.example.mythos.data.repositories.ChapterRepository
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NovelViewModel : ViewModel() {

    private val novelRepository = NovelRepository()
    private val chapterRepository = ChapterRepository()

    private val _novel = MutableStateFlow<NovelDetailDto?>(null)
    val novel: StateFlow<NovelDetailDto?> = _novel

    private val _chapters = MutableStateFlow<List<ChapterDto>>(emptyList())
    val chapters: StateFlow<List<ChapterDto>> = _chapters

    fun loadNovelById(id: String) {
        viewModelScope.launch {
            try {
                val result = novelRepository.getNovelById(id)
                _novel.value = result
                loadChaptersByNovelId(id)
            } catch (e: Exception) {
                e.printStackTrace()
                _novel.value = null
            }
        }
    }

    private suspend fun loadChaptersByNovelId(novelId: String) {
        try {
            val result = chapterRepository.getChaptersByNovelId(novelId)
            _chapters.value = result
        } catch (e: Exception) {
            e.printStackTrace()
            _chapters.value = emptyList()
        }
    }
}