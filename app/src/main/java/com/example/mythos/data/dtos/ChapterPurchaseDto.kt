package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ChapterPurchaseDto(
    val contentId: String,
    val totalPurchases: Int,
    val totalMythras: Int,
    val pricePerPurchase: Int
)
