package com.example.mythos.data.repositories

import com.example.mythos.data.dtos.ChapterDto
import com.example.mythos.data.dtos.PurchaseChapterRequest
import com.example.mythos.data.dtos.PurchaseResultDto
import com.example.mythos.data.managers.TokenManager
import com.example.mythos.data.network.NetworkModule
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class ChapterRepository {

    private val nodeClient = NetworkModule.nodeClient
    private val dotnetClient = NetworkModule.dotnetClient

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

    suspend fun getPurchasedChapters() : List<String> {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = dotnetClient.get("${NetworkModule.DOTNET_BASE_URL}/purchases/contents") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al obtener compras: ${response.status}")
        }

        return response.body()
    }

    suspend fun purchaseChapterAsync(chapterId: String, writerId: String, price: Int): PurchaseResultDto {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val requestBody = PurchaseChapterRequest(
            contentId = chapterId,
            writerId = writerId,
            price = price
        )

        val response: HttpResponse = dotnetClient.post("${NetworkModule.DOTNET_BASE_URL}/purchases/buy") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
            setBody(requestBody) // ✅ Usar DTO serializable
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al comprar capítulo: ${response.status}")
        }

        return response.body() ?: PurchaseResultDto(false, "Respuesta nula")
    }


}