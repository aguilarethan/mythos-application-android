package com.example.mythos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChapterCard(
    chapterNumber: Int,
    chapterTitle: String,
    chapterStatus: ChapterStatus,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Capítulo $chapterNumber",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chapterTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = getChapterStatusLabel(chapterStatus),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun getChapterStatusLabel(status: ChapterStatus): String {
    return when (status) {
        ChapterStatus.FREE -> "Gratis"
        ChapterStatus.PAID -> "De pago"
        ChapterStatus.PURCHASED -> "Comprado"
    }
}

enum class ChapterStatus {
    FREE, PURCHASED, PAID
}
