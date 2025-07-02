package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ChapterDto(
    val novelId: String,
    val volumeId: String? = null,
    val chapterNumber: Int,
    val title: String,
    val content: String,
    val priceMythras: Int,
    val createdAt: String,
    val updatedAt: String,
    val id: String,
    val isFree: Boolean
)
