package com.example.mythos.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mythos.data.dtos.NovelPreviewDto
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val novelRepository = NovelRepository()

    private val GENRES = listOf("Acción", "Aventura", "Romance", "Terror", "Drama", "Fantasía",
        "Fantasía oscura", "Ciencia ficción", "Comedia", "Misterio", "Misterio sobrenatural",
        "Cultivo", "Superheroes")

    private val _genres = MutableStateFlow(GENRES)
    val genres: StateFlow<List<String>> = _genres

    private val _recentNovels = MutableStateFlow<List<NovelPreviewDto>>(emptyList())
    val recentNovels: StateFlow<List<NovelPreviewDto>> = _recentNovels

    private val _mostViewedNovels = MutableStateFlow<List<NovelPreviewDto>>(emptyList())
    val mostViewedNovels: StateFlow<List<NovelPreviewDto>> = _mostViewedNovels

    fun loadLastThreeNovels() {
        viewModelScope.launch {
            try {
                val previews = novelRepository.getLastThreePreviews()
                _recentNovels.value = previews
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMostViewedNovels() {
        viewModelScope.launch {
            try {
                val previews = novelRepository.getEightMostViewedNovelsPreview()
                _mostViewedNovels.value = previews
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}