package com.example.mythos.data.dtos

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class PersonDto(
    val name: String,
    val lastName: String,
    val birthDate: LocalDate,
    val country: String,
    val biography: String
)
