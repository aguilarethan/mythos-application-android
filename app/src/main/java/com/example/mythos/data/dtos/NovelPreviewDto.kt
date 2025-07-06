package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovelPreviewDto(
    val writerAccountId: String,
    val writerName: String? = "",
    val title: String,
    val description: String? = "",
    val genres: List<String>? = listOf(""),
    val coverImageUrl: String,
    val id: String
)
