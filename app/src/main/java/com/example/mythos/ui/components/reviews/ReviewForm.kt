package com.example.mythos.ui.components.reviews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mythos.ui.components.RatingBar

@Composable
fun ReviewForm(
    onSubmit: (comment: String, rating: Int) -> Unit
) {
    var comment by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {

        Text("Tu reseña", style = MaterialTheme.typography.titleMedium)

        // Campo de texto para comentario
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comentario") },
            placeholder = { Text("¿Qué te pareció la novela?") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false,
            maxLines = 5
        )

        // Barra de calificación
        RatingBar(
            rating = rating,
            onRatingChanged = { rating = it }
        )

        // Botón para enviar
        Button(
            onClick = {
                if (comment.isNotBlank() && rating > 0) {
                    onSubmit(comment.trim(), rating)
                    comment = ""
                    rating = 0
                }
            },
            enabled = comment.isNotBlank() && rating > 0
        ) {
            Text("Enviar")
        }
    }
}