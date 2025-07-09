package com.example.mythos.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.NovelPreviewDto
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val novelRepository: NovelRepository = NovelRepository()
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<NovelPreviewDto>>(emptyList())
    val searchResults: StateFlow<List<NovelPreviewDto>> = _searchResults

    fun searchNovels(query: String) {
        viewModelScope.launch {
            try {
                val results = novelRepository.getNovelsByTitleMatch(query)
                _searchResults.value = results
            } catch (e: Exception) {
                e.printStackTrace()
                _searchResults.value = emptyList()
            }
        }
    }
}
