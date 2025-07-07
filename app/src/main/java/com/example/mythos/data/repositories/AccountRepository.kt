package com.example.mythos.data.repositories

import com.example.mythos.data.dtos.AccountDto
import com.example.mythos.data.dtos.PersonDto
import com.example.mythos.data.managers.TokenManager
import com.example.mythos.data.network.NetworkModule
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AccountRepository {

    private val dotnetClient = NetworkModule.dotnetClient

    suspend fun getAccountByToken(): AccountDto {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = dotnetClient.get("${NetworkModule.DOTNET_BASE_URL}/account") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al recuperar la cuenta: ${response.status}")
        }

        return response.body()
    }

    suspend fun becomeWriter(personDto: PersonDto) {
        val accessToken = TokenManager.getAccessToken()
            ?: throw Exception("No hay token de acceso disponible")

        val response: HttpResponse = dotnetClient.post("${NetworkModule.DOTNET_BASE_URL}/account/become-writer") {
            contentType(ContentType.Application.Json)
            headers {
                append("Authorization", "Bearer $accessToken")
            }
            setBody(personDto)
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al convertirte en escritor: ${response.status}")
        }
        println(response.bodyAsText())

        return response.body()
    }
}