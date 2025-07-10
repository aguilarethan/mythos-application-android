package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ChapterStatsDto(
    val id: String,
    val title: String,
    val chapterNumber: Int,
    val priceMythras: Int,
    val totalPurchases: Int,
    val totalMythras: Int
)
