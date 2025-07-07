package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val accountId: String,
    val username: String,
    val email: String,
    val password: String,
    val role: String
)
