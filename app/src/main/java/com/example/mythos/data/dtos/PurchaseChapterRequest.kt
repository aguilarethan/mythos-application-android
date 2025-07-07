package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable

data class PurchaseChapterRequest(
    val contentId: String,
    val writerId: String,
    val price: Int
)
