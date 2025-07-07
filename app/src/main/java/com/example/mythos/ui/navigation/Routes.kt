package com.example.mythos.ui.navigation

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val REGISTER = "register"
    const val SEARCH = "search"
    const val PROFILE = "profile"
    const val BECOME_WRITER = "profile/becomewriter"

    fun myNovelsWithId(id: String) = "profile/mynovels/$id"
    fun novelWithId(id: String) = "novel/$id"
    fun chapterWithId(id: String) = "chapter/$id"
    fun purchaseChapterWithId(id: String, writerId: String) = "purchase/$id/$writerId"
}