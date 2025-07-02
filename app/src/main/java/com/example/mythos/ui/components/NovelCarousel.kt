package com.example.mythos.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mythos.data.dtos.NovelPreviewDto

@Composable
fun NovelCarousel(
    novels: List<NovelPreviewDto>,
    onItemClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(novels) { novel ->
            Card(
                onClick = { onItemClick(novel.id) },
                modifier = Modifier
                    .width(350.dp)
                    .height(180.dp)
            ) {
                Row (modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = novel.coverImageUrl,
                        contentDescription = novel.title,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(120.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column (
                        modifier = Modifier.fillMaxHeight().padding(12.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = novel.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = novel.description.take(150) + "...",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 4
                        )
                    }
                }
            }
        }
    }
}


