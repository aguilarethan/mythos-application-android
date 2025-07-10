package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ReviewDto(
    val accountId: String = "",
    val comment: String = "",
    val novelId: String = "",
    val rating: Int = 0,
    val createdAt: String = "",
    val username: String = ""
)
