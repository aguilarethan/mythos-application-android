package com.example.mythos.data.managers

import com.example.mythos.data.dtos.ChapterFormDto

object ChapterManager {
    var currentNovelIdForChapter: String? = null
        private set

    var currentChapterForEditing: ChapterFormDto? = null
        private set

    fun setNovelForChapter(novelId: String) {
        currentNovelIdForChapter = novelId
    }

    fun setChapterForEditing(chapter: ChapterFormDto) {
        currentChapterForEditing = chapter
        currentNovelIdForChapter = chapter.novelId
    }

    fun clearChapterData() {
        currentNovelIdForChapter = null
        currentChapterForEditing = null
    }
}