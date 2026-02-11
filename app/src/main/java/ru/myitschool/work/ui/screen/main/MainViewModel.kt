package ru.myitschool.work.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.myitschool.work.domain.main.GetUserInfoUseCase

class MainViewModel(
    private val authCode: String
) : ViewModel() {

    private val getUserInfoUseCase = GetUserInfoUseCase()

    private val _uiState = MutableStateFlow<MainState>(MainState.Loading)
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    init {
        loadUserInfo()
    }

    fun onRefresh() {
        loadUserInfo()
    }

    fun onLogout() {
    }

    fun onBookClick() {
    }


    private fun loadUserInfo() {
        viewModelScope.launch {
            getUserInfoUseCase.invoke(authCode).fold(
                onSuccess = { user ->
                    val bookings = user.bookingList.map { bookingEntity ->
                        BookingItem(
                            date = formatDate(bookingEntity.time),
                            place = bookingEntity.roomName
                        )
                    }.sortedBy { it.date }

                    _uiState.value = MainState.Success(
                        name = user.name,
                        photoUrl = user.photoUrl,
                        bookings = bookings
                    )
                },
                onFailure = { error ->
                    val msg = when {
                        error.message?.contains("401") == true -> "Пользователь не найден"
                        else -> "Не удалось загрузить данные"
                    }
                    _uiState.value = MainState.Error(msg)
                }
            )
        }
    }

    private fun formatDate(input: String): String {
        return try {
            val parts = input.split("-")
            if (parts.size == 3) {
                "${parts[2]}.${parts[1]}.${parts[0]}"
            } else {
                input
            }
        } catch (e: Exception) {
            input
        }
    }
}