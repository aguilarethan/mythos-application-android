package com.example.mythos.data.repositories

import com.example.mythos.data.dtos.ChapterDto
import com.example.mythos.data.network.NetworkModule
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ChapterRepository {

    private val nodeClient = NetworkModule.nodeClient

    suspend fun getChaptersByNovelId(novelId: String): List<ChapterDto> {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/chapters/novel/${novelId}") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener los capitulos: ${response.status}")
        }

        return response.body()
    }

    suspend fun getChapterById(chapterId: String): ChapterDto {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/chapters/${chapterId}") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener el capitulo: ${response.status}")
        }

        return response.body()
    }



}