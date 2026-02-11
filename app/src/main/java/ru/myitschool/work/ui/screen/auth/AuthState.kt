package ru.myitschool.work.ui.screen.auth

sealed class AuthState {
    object Loading : AuthState()
    data class Data(
        val error: String? = null
    ) : AuthState()
}