package com.example.mythos.ui.components.reviews

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.ReviewDto
import com.example.mythos.data.managers.AccountManager
import com.example.mythos.data.repositories.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewsContainerViewModel : ViewModel() {

    private val reviewRepository = ReviewRepository()

    private val _reviews = MutableStateFlow<List<ReviewDto>>(emptyList())
    val reviews: StateFlow<List<ReviewDto>> = _reviews

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadReviewsByNovelId(novelId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = reviewRepository.getReviewsByNovelId(novelId)
                _reviews.value = result
            } catch (e: Exception) {
                Log.e("ReviewsViewModel", "Error al cargar reseñas", e)
                e.printStackTrace()
                _error.value = e.message
                _reviews.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitReview(novelId: String, comment: String, rating: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                //val accountId = TokenManager.getCurrentUserId() ?: return@launch
                val accountId = AccountManager.getCurrentUserId() ?: return@launch
                //val username = TokenManager.getCurrentUsername() ?: "Anónimo"
                val username = AccountManager.getCurrentUserName() ?: "Anónimo"
                val review = ReviewDto(
                    accountId = accountId,
                    comment = comment,
                    novelId = novelId,
                    rating = rating,
                    username = username
                )

                val success = reviewRepository.createReview(review)
                if (success) {
                    onSuccess()
                } else {
                    _error.value = "Error al crear la reseña"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}