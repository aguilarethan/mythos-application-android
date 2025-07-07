package com.example.mythos.ui.screens.becomewriter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.PersonDto
import com.example.mythos.data.managers.AccountManager
import com.example.mythos.data.repositories.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class BecomeWriterViewModel : ViewModel() {

    private val accountRepository = AccountRepository()

    // Estado del formulario
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _lastName = MutableStateFlow("")
    val lastName: StateFlow<String> = _lastName.asStateFlow()

    private val _birthDate = MutableStateFlow<LocalDate?>(null)
    val birthDate: StateFlow<LocalDate?> = _birthDate.asStateFlow()

    private val _birthDateText = MutableStateFlow("")
    val birthDateText: StateFlow<String> = _birthDateText.asStateFlow()

    private val _country = MutableStateFlow("")
    val country: StateFlow<String> = _country.asStateFlow()

    private val _biography = MutableStateFlow("")
    val biography: StateFlow<String> = _biography.asStateFlow()

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    // Métodos para actualizar el formulario
    fun updateName(newName: String) {
        _name.value = newName
        validateForm()
    }

    fun updateLastName(newLastName: String) {
        _lastName.value = newLastName
        validateForm()
    }

    fun updateBirthDate(newBirthDate: LocalDate?) {
        _birthDate.value = newBirthDate
        validateForm()
    }

    fun updateBirthDateText(newText: String) {
        _birthDateText.value = newText
        validateForm()
    }

    fun updateCountry(newCountry: String) {
        _country.value = newCountry
        validateForm()
    }

    fun updateBiography(newBiography: String) {
        _biography.value = newBiography
        validateForm()
    }

    // Validación del formulario
    private fun validateForm() {
        val parsedDate = try {
            if (_birthDateText.value.isNotBlank()) {
                LocalDate.parse(_birthDateText.value)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

        _birthDate.value = parsedDate

        val isValid = _name.value.isNotBlank() &&
                _lastName.value.isNotBlank() &&
                parsedDate != null &&
                _country.value.isNotBlank() &&
                _biography.value.isNotBlank()

        _isFormValid.value = isValid
    }


    // Método para convertirse en escritor
    fun becomeWriter() {
        if (!_isFormValid.value) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val personDto = PersonDto(
                    name = _name.value.trim(),
                    lastName = _lastName.value.trim(),
                    birthDate = _birthDate.value!!,
                    country = _country.value.trim(),
                    biography = _biography.value.trim()
                )

                // Llamar al repositorio para convertirse en escritor
                accountRepository.becomeWriter(personDto)

                // Recargar la cuenta para actualizar el rol
                AccountManager.loadAccount()

                _isSuccess.value = true

            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error desconocido al convertirse en escritor"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Limpiar error
    fun clearError() {
        _errorMessage.value = null
    }

    // Resetear estado de éxito
    fun resetSuccess() {
        _isSuccess.value = false
    }

    // Validaciones específicas
    fun isNameValid(): Boolean = _name.value.isNotBlank()
    fun isLastNameValid(): Boolean = _lastName.value.isNotBlank()
    fun isBirthDateValid(): Boolean = _birthDate.value != null
    fun isCountryValid(): Boolean = _country.value.isNotBlank()
    fun isBiographyValid(): Boolean = _biography.value.isNotBlank()

    // Obtener mensajes de error específicos
    fun getNameError(): String? = if (_name.value.isBlank() && _name.value.isNotEmpty()) "El nombre es requerido" else null
    fun getLastNameError(): String? = if (_lastName.value.isBlank() && _lastName.value.isNotEmpty()) "El apellido es requerido" else null
    fun getBirthDateError(): String? {
        return when {
            _birthDateText.value.isBlank() -> "La fecha de nacimiento es requerida"
            _birthDate.value == null -> "Formato inválido (usa YYYY-MM-DD)"
            else -> null
        }
    }
    fun getCountryError(): String? = if (_country.value.isBlank() && _country.value.isNotEmpty()) "El país es requerido" else null
    fun getBiographyError(): String? = if (_biography.value.isBlank() && _biography.value.isNotEmpty()) "La biografía es requerida" else null
}