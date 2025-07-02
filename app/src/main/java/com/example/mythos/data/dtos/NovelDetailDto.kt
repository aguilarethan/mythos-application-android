package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class NovelDetailDto(
    val id: String,
    val writerAccountId: String,
    val title: String,
    val description: String,
    val genres: List<List<String>>,
    val tags: List<String>,
    val views: Int,
    val isPublic: Boolean,
    val coverImageUrl: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
