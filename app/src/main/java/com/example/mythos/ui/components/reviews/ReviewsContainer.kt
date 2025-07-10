package com.example.mythos.ui.components.reviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewsContainer(
    novelId: String,
    viewModel: ReviewsContainerViewModel = viewModel()
) {

    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var showForm by remember { mutableStateOf(false) }

    LaunchedEffect(novelId) {
        viewModel.loadReviewsByNovelId(novelId)
    }

    Column  {

        Button(onClick = { showForm = !showForm }) {
            Text(if (showForm) "Cancelar" else "Agregar reseña")
        }

        // Formulario visible condicionalmente
        if (showForm) {
            ReviewForm(onSubmit = { comment, rating ->
                viewModel.submitReview(
                    novelId = novelId,
                    comment = comment,
                    rating = rating,
                    onSuccess = {
                        showForm = false
                        viewModel.loadReviewsByNovelId(novelId) // Recargar reseñas
                    }
                )
            })
        }

        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            error != null -> {
                Text(text = "Error: $error")
            }

            reviews.isEmpty() -> {
                Text("Aun no hay reseñas para esta novela")
            }

            else -> {
                Column (
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    reviews.forEach { review ->
                        ReviewCard(review = review)
                    }
                }
            }
        }
    }

}