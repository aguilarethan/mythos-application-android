package com.example.mythos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

@Composable
fun Avatar(username: String) {

    val initial = username.firstOrNull()?.uppercase() ?: "?"

    Box (
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(getBackgroundColor(username)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getBackgroundColor(name: String): Color {
    val colors = listOf(
        Color(0xFFEF5350),
        Color(0xFF42A5F5),
        Color(0xFF66BB6A),
        Color(0xFFFFA726),
        Color(0xFFAB47BC)
    )
    val index = name.hashCode().absoluteValue % colors.size
    return colors[index]
}
