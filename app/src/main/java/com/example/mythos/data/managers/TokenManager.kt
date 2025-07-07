package com.example.mythos.data.managers

import android.content.Context
import android.content.SharedPreferences

object TokenManager {

    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"

    private var sharedPreferences: SharedPreferences? = null

    fun initialize(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(
                "auth_tokens",
                Context.MODE_PRIVATE
            )
        }
    }

    fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences?.edit()?.apply {
            putString(ACCESS_TOKEN_KEY, accessToken)
            putString(REFRESH_TOKEN_KEY, refreshToken)
            apply()
        }
    }

    fun getAccessToken(): String? {
        return sharedPreferences?.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences?.getString(REFRESH_TOKEN_KEY, null)
    }

    fun clearTokens() {
        sharedPreferences?.edit()?.clear()?.apply()
    }

    fun hasToken(): Boolean {
        return !getAccessToken().isNullOrEmpty()
    }
}
