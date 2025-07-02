package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovelPreviewDto(
    val writerAccountId: String,
    val title: String,
    val description: String,
    val coverImageUrl: String,
    val id: String
)
