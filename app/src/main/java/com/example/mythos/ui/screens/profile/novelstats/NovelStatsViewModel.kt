package com.example.mythos.ui.screens.profile.novelstats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.NovelStatsDto
import com.example.mythos.data.repositories.NovelRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NovelStatsViewModel : ViewModel() {

    private val novelRepository = NovelRepository()

    suspend fun getAuthorNovelStats(
        writerAccountId: String,
        startDate: String,
        endDate: String
    ): List<NovelStatsDto> = withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
        novelRepository.getAuthorNovelStats(
            writerAccountId = writerAccountId,
            startDate = startDate,
            endDate = endDate
        )
    }
}
