package com.example.mythos.ui.screens.profile.mynovels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.NovelPreviewDto
import com.example.mythos.data.managers.AccountManager
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyNovelsViewModel : ViewModel() {

    private val novelRepository = NovelRepository()

    private val _myNovels = MutableStateFlow<List<NovelPreviewDto>>(emptyList())
    val myNovels: StateFlow<List<NovelPreviewDto>> = _myNovels

    fun loadMyNovels(writerAccountId: String) {
        viewModelScope.launch {
            try {
                val previews = novelRepository.getNovelsByWriterAccountId(writerAccountId)
                _myNovels.value = previews
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}