package com.example.mythos.data.repositories

import com.example.mythos.data.dtos.AuthorChapterStatsRequestDto
import com.example.mythos.data.dtos.ChapterPurchaseDto
import com.example.mythos.data.dtos.CreateNovelDto
import com.example.mythos.data.dtos.NovelDetailDto
import com.example.mythos.data.dtos.NovelFormDto
import com.example.mythos.data.network.NetworkModule
import com.example.mythos.data.dtos.NovelPreviewDto
import com.example.mythos.data.dtos.NovelStatsDto
import com.example.mythos.data.dtos.UpdateNovelDto
import com.example.mythos.data.managers.TokenManager
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.*
import io.ktor.http.*

class NovelRepository {

    private val nodeClient = NetworkModule.nodeClient
    private val dotnetClient = NetworkModule.dotnetClient


    suspend fun getLastThreePreviews(): List<NovelPreviewDto> {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/novels/search/last-three-preview") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener novelas: ${response.status}")
        }

        return response.body()
    }

    suspend fun getNovelById(id: String): NovelDetailDto {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/novels/$id") {
            accept(ContentType.Application.Json)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener novela con ID $id: ${response.status}")
        }

        return response.body()
    }

    suspend fun getEightMostViewedNovelsPreview(): List<NovelPreviewDto> {
        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/novels/search/eight-most-viewed-preview") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener novelas: ${response.status}")
        }

        return response.body()
    }

    suspend fun getNovelsByTitleMatch(title: String): List<NovelPreviewDto> {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/novels/search/title/$title") {
            accept(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("No se pudieron recuperar las novelas del escritor")
        }

        return response.body()
    }

    suspend fun getNovelsByWriterAccountId(accountId: String): List<NovelPreviewDto> {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/novels/search/writer/$accountId") {
            accept(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("No se pudieron recuperar las novelas del escritor")
        }

        return response.body()
    }

    suspend fun uploadCoverImage(coverImageBytes: ByteArray, fileName: String): String {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = nodeClient.submitFormWithBinaryData(
            url = "${NetworkModule.NODE_BASE_URL}/novels/upload/cover-image",
            formData = formData {
                append("coverImage", coverImageBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/jpeg")
                    append(HttpHeaders.ContentDisposition, "filename=$fileName")
                })
            }
        ) {
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }

        if (!response.status.isSuccess()) {
            val errorText = response.bodyAsText()
            throw Exception("Error al subir la imagen de portada: $errorText")
        }

        return response.bodyAsText()
    }

    suspend fun createNovel(data: NovelFormDto): NovelFormDto {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val request = CreateNovelDto(
            writerAccountId = data.writerAccountId.toString(),
            writerName = data.writerName,
            title = data.title,
            description = data.description,
            genres = data.genres,
            tags = data.tags,
            coverImageUrl = data.coverImageUrl.trim('"')
        )



        val response: HttpResponse = nodeClient.post("${NetworkModule.NODE_BASE_URL}/novels") {
            contentType(ContentType.Application.Json)
            setBody(request)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }

        if (!response.status.isSuccess()) {
            val errorText = response.bodyAsText()
            throw Exception("Error al crear la novela: $errorText")
        }

        return response.body()
    }

    suspend fun updateNovel(id: String, data: NovelFormDto): String {

        val request = UpdateNovelDto(
            writerAccountId = data.writerAccountId.toString(),
            writerName = data.writerName,
            title = data.title,
            description = data.description,
            genres = data.genres,
            tags = data.tags,
            coverImageUrl = data.coverImageUrl.trim('"')
        )
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = nodeClient.put("${NetworkModule.NODE_BASE_URL}/novels/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }

        if (!response.status.isSuccess()) {
            val errorText = response.bodyAsText()
            throw Exception("Error al actualizar la novela: $errorText")
        }

        return response.body()
    }

    suspend fun getAuthorNovelStats(
        writerAccountId: String,
        startDate: String,
        endDate: String
    ): List<NovelStatsDto> {

        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        // 1. Obtener estadísticas de capítulos desde el backend .NET
        val chapterStats: List<ChapterPurchaseDto> = dotnetClient.get("${NetworkModule.DOTNET_BASE_URL}/purchases/statistics-author") {
            url {
                parameters.append("startDate", startDate)
                parameters.append("endDate", endDate)
            }
            accept(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }.let { response ->
            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText()
                throw Exception("Error al obtener estadísticas de capítulos: $errorText")
            }
            response.body()
        }

        // 2. Preparar el cuerpo de la solicitud al backend Node.js
        val requestBody = AuthorChapterStatsRequestDto(
            writerAccountId = writerAccountId,
            contentStats = chapterStats
        )

        // 3. Hacer POST al backend Node.js para obtener estadísticas por novela
        val novelStats: List<NovelStatsDto> = nodeClient.post("${NetworkModule.NODE_BASE_URL}/novels/author/chapters/stats") {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }.let { response ->
            if (!response.status.isSuccess()) {
                val errorText = response.bodyAsText()
                throw Exception("Error al recuperar estadísticas de novelas: $errorText")
            }
            response.body()
        }

        return novelStats
    }

}