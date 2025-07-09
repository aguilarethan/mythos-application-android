package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ChapterFormDto(
    val novelId: String,
    val title: String = "",
    val content: String = "",
    val priceMythras: Int = 0,
)
