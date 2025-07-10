package com.example.mythos.data.repositories

import com.example.mythos.data.dtos.ReviewDto
import com.example.mythos.data.managers.TokenManager
import com.example.mythos.data.network.NetworkModule
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.*

class ReviewRepository {

    private val nodeClient = NetworkModule.nodeClient

    suspend fun getReviewsByNovelId(novelId: String): List<ReviewDto> {

        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response : HttpResponse = nodeClient.get("${NetworkModule.NODE_BASE_URL}/reviews/search/novel-id/$novelId") {
            accept(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        }

        if (!response.status.isSuccess())
            throw Exception("Error al obtener reseñas: ${response.status}")

        return response.body()
    }

    suspend fun createReview(review: ReviewDto): Boolean {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = nodeClient.post("${NetworkModule.NODE_BASE_URL}/reviews") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
            setBody(review)
        }

        return response.status.isSuccess()
    }
}