package com.example.mythos.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun RatingBar(
    rating: Int,
    onRatingChanged: ((Int) -> Unit)? = null,
    maxRating: Int = 5,
    starSize: Dp = 32.dp,
    spacing: Dp = 4.dp,
    activeColor: Color = Color(0xFFFFD700),
    inactiveColor: Color = Color.LightGray
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        for (i in 1..maxRating) {
            val icon = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5f -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }

            Icon(
                imageVector = icon,
                contentDescription = "Estrella $i",
                tint = if (rating >= i - 0.5f) activeColor else inactiveColor,
                modifier = Modifier
                    .size(starSize)
                    .then(
                        if (onRatingChanged != null) Modifier.clickable { onRatingChanged(i) }
                        else Modifier
                    )
            )
        }
    }
}