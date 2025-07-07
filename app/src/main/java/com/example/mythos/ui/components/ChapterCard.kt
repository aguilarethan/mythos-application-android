package com.example.mythos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Capítulo $chapterNumber",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                ChapterStatusChip(status = chapterStatus)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = chapterTitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ChapterStatusChip(status: ChapterStatus) {
    val label = when (status) {
        ChapterStatus.FREE -> "Gratis"
        ChapterStatus.PAID -> "De pago"
        ChapterStatus.PURCHASED -> "Comprado"
    }

    val color = when (status) {
        ChapterStatus.FREE -> MaterialTheme.colorScheme.primary
        ChapterStatus.PAID -> MaterialTheme.colorScheme.secondary
        ChapterStatus.PURCHASED -> MaterialTheme.colorScheme.error
    }

    AssistChip(
        onClick = {},
        label = { Text(label)},
        colors = AssistChipDefaults.assistChipColors(containerColor = color)
    )
}

enum class ChapterStatus {
    FREE, PURCHASED, PAID
}