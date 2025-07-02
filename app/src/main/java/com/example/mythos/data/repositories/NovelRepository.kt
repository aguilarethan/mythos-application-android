package com.example.mythos.data.repositories

import com.example.mythos.data.dtos.NovelDetailDto
import com.example.mythos.data.network.NetworkModule
import com.example.mythos.data.dtos.NovelPreviewDto
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class NovelRepository {

    private val nodeClient = NetworkModule.nodeClient

    suspend fun getLastThreePreviews(): List<NovelPreviewDto> {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/api/novels/search/last-three-preview") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener novelas: ${response.status}")
        }

        return response.body()
    }

    suspend fun getNovelById(id: String): NovelDetailDto {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/api/novels/$id") {
            accept(ContentType.Application.Json)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener novela con ID $id: ${response.status}")
        }

        return response.body()
    }

}