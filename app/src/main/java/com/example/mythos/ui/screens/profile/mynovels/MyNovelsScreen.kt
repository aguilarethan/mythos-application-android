package com.example.mythos.ui.screens.profile.mynovels

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.copy
import com.example.mythos.ui.components.MostViewedNovelsPreview
import com.example.mythos.ui.components.NovelCarousel
import com.example.mythos.ui.screens.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyNovelsScreen(
    viewModel: MyNovelsViewModel,
    writerAccountId: String,
    onNovelClick: (String) -> Unit,
    onNavigateToNovelForm: () -> Unit
) {
    val myNovels by viewModel.myNovels.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyNovels(writerAccountId)
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { onNavigateToNovelForm() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("Crear nueva novela")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                top = 0.dp,
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                bottom = innerPadding.calculateBottomPadding()
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Text(
                    text = "Mis novelas",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
            }

            item {
                MostViewedNovelsPreview(novels = myNovels, onItemClick = onNovelClick)
            }
        }
    }
}
