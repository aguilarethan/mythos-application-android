package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AuthorChapterStatsRequestDto(
    val writerAccountId: String,
    val contentStats: List<ChapterPurchaseDto>
)
