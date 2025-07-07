package com.example.mythos.data.managers

import com.example.mythos.data.dtos.AccountDto
import com.example.mythos.data.repositories.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AccountManager {

    private val accountRepository = AccountRepository()

    private val _currentAccount = MutableStateFlow<AccountDto?>(null)
    val currentAccount: StateFlow<AccountDto?> = _currentAccount.asStateFlow()

    private val _isAccountLoaded = MutableStateFlow(false)
    val isAccountLoaded: StateFlow<Boolean> = _isAccountLoaded.asStateFlow()

    private val _accountError = MutableStateFlow<String?>(null)
    val accountError: StateFlow<String?> = _accountError.asStateFlow()

    suspend fun loadAccount(): Result<AccountDto> {
        return try {
            _accountError.value = null
            val account = accountRepository.getAccountByToken()
            _currentAccount.value = account
            _isAccountLoaded.value = true
            Result.success(account)
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Error desconocido al cargar la cuenta"
            _accountError.value = errorMessage
            _isAccountLoaded.value = false
            Result.failure(e)
        }
    }

    fun clearAccount() {
        _currentAccount.value = null
        _isAccountLoaded.value = false
        _accountError.value = null
    }

    fun getCurrentAccount(): AccountDto? {
        return _currentAccount.value
    }

    fun isAccountAvailable(): Boolean {
        return _currentAccount.value != null
    }

    fun clearError() {
        _accountError.value = null
    }

    fun getCurrentUserId(): String? {
        return _currentAccount.value?.accountId
    }

    fun getCurrentUserName(): String? {
        return _currentAccount.value?.username
    }

    fun getCurrentUserEmail(): String? {
        return _currentAccount.value?.email
    }

    suspend fun updateAccountProperty(updatedAccount: AccountDto) {
        _currentAccount.value = updatedAccount
    }
}