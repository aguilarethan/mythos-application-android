package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreateNovelDto(
    val writerAccountId: String,
    val writerName: String,
    val title: String,
    val description: String,
    val genres: List<String>,
    val tags: List<String>,
    val coverImageUrl: String,
)
