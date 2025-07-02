package com.example.mythos.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.NovelPreviewDto
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val novelRepository = NovelRepository()

    private val _novels = MutableStateFlow<List<NovelPreviewDto>>(emptyList())
    val novels: StateFlow<List<NovelPreviewDto>> = _novels

    fun loadLastThreeNovels() {
        viewModelScope.launch {
            try {
                val previews = novelRepository.getLastThreePreviews()
                _novels.value = previews
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}