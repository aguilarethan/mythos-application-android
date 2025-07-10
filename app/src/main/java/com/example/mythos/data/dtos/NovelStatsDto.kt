package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovelStatsDto(
    val novelId: String,
    val title: String,
    val coverImageUrl: String,
    val totalMythras: Int,
    val chapters: List<ChapterStatsDto>
)
