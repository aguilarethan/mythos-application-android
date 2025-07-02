package com.example.mythos.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mythos.ui.components.NovelCarousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNovelClick: (String) -> Unit
) {
    val novels by viewModel.novels.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLastThreeNovels()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Descubre lo más reciente",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        NovelCarousel(novels = novels, onItemClick = onNovelClick)
    }
}
