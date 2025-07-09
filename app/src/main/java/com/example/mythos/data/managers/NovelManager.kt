package com.example.mythos.data.managers

import com.example.mythos.data.dtos.NovelFormDto

object NovelManager {
    private var _currentNovelForEditing: NovelFormDto? = null

    val currentNovelForEditing: NovelFormDto?
        get() = _currentNovelForEditing

    fun setNovelForEditing(novel: NovelFormDto) {
        _currentNovelForEditing = novel
    }

    fun clearNovelForEditing() {
        _currentNovelForEditing = null
    }

    fun isEditingMode(): Boolean {
        return _currentNovelForEditing != null
    }
}