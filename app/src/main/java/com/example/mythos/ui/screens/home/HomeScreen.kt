package com.example.mythos.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mythos.ui.components.MostViewedNovelsPreview
import com.example.mythos.ui.components.NovelCarousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNovelClick: (String) -> Unit
) {
    val genres by viewModel.genres.collectAsState()
    val recentNovels by viewModel.recentNovels.collectAsState()
    val mostViewedModels by viewModel.mostViewedNovels.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLastThreeNovels()
        viewModel.loadMostViewedNovels()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(0.dp)
    ) {
        item {
            Text(
                text = "Descubre lo más reciente",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            NovelCarousel(novels = recentNovels, onItemClick = onNovelClick)
        }

        item {
            Text(
                text = "Más vistas",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        item {
            MostViewedNovelsPreview(novels = mostViewedModels, onItemClick = onNovelClick)
        }

    }
}
