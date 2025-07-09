package com.example.mythos.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mythos.data.dtos.AccountDto
import com.example.mythos.data.managers.AccountManager
import com.example.mythos.data.managers.TokenManager
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    val currentAccount: StateFlow<AccountDto?> = AccountManager.currentAccount
    val isAccountLoaded: StateFlow<Boolean> = AccountManager.isAccountLoaded
    val accountError: StateFlow<String?> = AccountManager.accountError

    // Métodos de conveniencia para acceder a los datos del usuario
    fun getCurrentAccount(): AccountDto? {
        return AccountManager.getCurrentAccount()
    }

    fun getCurrentUserId(): String? {
        return AccountManager.getCurrentUserId()
    }

    fun getCurrentUserName(): String? {
        return AccountManager.getCurrentUserName()
    }

    fun getCurrentUserEmail(): String? {
        return AccountManager.getCurrentUserEmail()
    }

    fun getCurrentUserRole(): String? {
        return AccountManager.getCurrentAccount()?.role
    }

    fun isAccountAvailable(): Boolean {
        return AccountManager.isAccountAvailable()
    }

    fun isUserAuthor(): Boolean {
        val role = getCurrentUserRole()?.lowercase()
        return role == "writer"
    }

    fun clearError() {
        AccountManager.clearError()
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            TokenManager.clearTokens()
            AccountManager.clearAccount()
            onLogoutComplete()
        }
    }

}