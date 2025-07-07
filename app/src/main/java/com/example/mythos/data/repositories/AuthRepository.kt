package com.example.mythos.data.repositories

import android.content.Context
import com.example.mythos.data.dtos.TokenResponseDto
import com.example.mythos.data.managers.TokenManager
import com.example.mythos.data.network.NetworkModule
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class AuthRepository() {

    private val dotnetClient = NetworkModule.dotnetClient
    private val tokenManager = TokenManager

    suspend fun login(username: String, password: String): TokenResponseDto {
        val response: HttpResponse = dotnetClient.post("${NetworkModule.DOTNET_BASE_URL}/auth/login-raw") {
            contentType(ContentType.Application.Json)
            setBody( mapOf("username" to username, "password" to password) )
        }

        if (!response.status.isSuccess()) {
            throw Exception("Error al iniciar sesión: ${response.status}")
        }

        val tokenResponse = response.body<TokenResponseDto>()

        tokenManager.saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken)

        return tokenResponse
    }

    suspend fun register(username: String, password: String, email: String) : String {
        try {
            val response: HttpResponse = dotnetClient.post("${NetworkModule.DOTNET_BASE_URL}/auth/register") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "username" to username,
                        "password" to password,
                        "email" to email
                    )
                )
            }

            return if (response.status.isSuccess()) {
                "Cuenta registrada con éxito"
            } else {
                val errorBody = response.bodyAsText()
                "Error en el registro: $errorBody"
            }

        } catch (e: ClientRequestException) {
            val errorMessage = e.response.bodyAsText()
            return "Error: $errorMessage"
        } catch (e: Exception) {
            return "Error inesperado: ${e.localizedMessage}"
        }
    }

    fun logout() {
        tokenManager.clearTokens()
    }

    fun isLoggedIn(): Boolean {
        return tokenManager.hasToken()
    }


}