package com.example.mythos.ui.components.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mythos.data.dtos.ReviewDto
import com.example.mythos.ui.components.Avatar
import com.example.mythos.ui.components.RatingBar

@Composable
fun ReviewCard(review : ReviewDto) {

    Card (
        Modifier
            .fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Avatar(username = review.username)

            Column {
                Text ( text = review.username, fontWeight = FontWeight.Bold)

                RatingBar(rating = review.rating, starSize = 20.dp)

                Text ( text = review.comment, fontSize = 14.sp, lineHeight = 15.sp)
            }
        }
    }

}