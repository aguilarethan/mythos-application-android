package com.example.mythos.ui.screens.search

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mythos.ui.components.MostViewedNovelsPreview

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNovelClick: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val novels by viewModel.searchResults.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                if (query.length > 2) {
                    viewModel.searchNovels(query)
                }
            },
            label = { Text("Buscar novela...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (novels.isNotEmpty()) {
            MostViewedNovelsPreview(novels = novels, onItemClick = onNovelClick)
        } else {
            Text("No hay resultados", style = MaterialTheme.typography.bodyMedium)
        }
    }
}