package com.example.mythos.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTER = "register"
    const val SEARCH = "search"
    const val PROFILE = "profile"

    fun novelWithId(id: String) = "novel/$id"
    fun chapterWithId(id: String) = "chapter/$id"
}