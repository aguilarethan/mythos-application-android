package com.example.mythos.ui.screens.novel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mythos.data.dtos.NovelDetailDto
import com.example.mythos.ui.components.ChapterCard

@Composable
fun NovelScreen(
    viewModel: NovelViewModel,
    novelId: String,
    onChapterClick: (String) -> Unit
) {
    val novel by viewModel.novel.collectAsState()
    val chapters by viewModel.chapters.collectAsState()

    var selectedTabIndex by remember { mutableStateOf(0) }


    LaunchedEffect(novelId) {
        viewModel.loadNovelById(novelId)
    }

    novel?.let { novel ->
        Column(modifier = Modifier.padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = novel.coverImageUrl,
                    contentDescription = novel.title,
                    modifier = Modifier
                        .size(width = 120.dp, height = 180.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        novel.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Autor: ${novel.writerName}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Estado: ${novel.status}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        "Vistas: ${novel.views}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val tabs = listOf("Información", "Capítulos")
            TabRow(
                selectedTabIndex = selectedTabIndex
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTabIndex) {
                0 -> {

                    Column {
                        Text("Descripción", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(novel.description, style = MaterialTheme.typography.bodyMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Géneros", style = MaterialTheme.typography.titleMedium)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(novel.genres) { genre ->
                                AssistChip(onClick = {}, label = { Text(genre) })
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Etiquetas", style = MaterialTheme.typography.titleMedium)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(novel.tags) { tag ->
                                AssistChip(onClick = {}, label = { Text(tag) })
                            }
                        }
                    }
                }

                1 -> {

                    if (chapters.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay capítulos disponibles.")
                        }
                    } else {
                        Column {
                            chapters.forEach { chapter ->
                                ChapterCard(
                                    chapterNumber = chapter.chapterNumber,
                                    chapterTitle = chapter.title,
                                    onClick = {
                                        onChapterClick(chapter.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}