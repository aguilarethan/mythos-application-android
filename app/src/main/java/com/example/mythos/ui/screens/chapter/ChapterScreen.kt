package com.example.mythos.ui.screens.chapter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mythos.data.dtos.ChapterDto

@Composable
fun ChapterScreen(
    viewModel: ChapterViewModel,
    chapterId: String
) {
    val chapter by viewModel.chapter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(chapterId) {
        viewModel.loadChapter(chapterId)
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: $errorMessage")
            }
        }
        chapter != null -> {
            ChapterContent(chapter = chapter!!)
        }
    }
}

@Composable
fun ChapterContent(chapter: ChapterDto) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row ( modifier = Modifier.fillMaxWidth()) {
            Text(
                text = chapter.chapterNumber.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = chapter.title,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = chapter.content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
