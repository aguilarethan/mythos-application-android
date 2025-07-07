package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class PurchaseResultDto(
    val success: Boolean,
    val message: String
)