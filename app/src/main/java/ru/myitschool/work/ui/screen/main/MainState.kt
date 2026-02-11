package ru.myitschool.work.ui.screen.main

sealed interface MainState {
    object Loading : MainState
    data class Success(
        val name: String,
        val photoUrl: String,
        val bookings: List<BookingItem>,
    ) : MainState

    data class Error(
        val message: String,
    ) : MainState
}

data class BookingItem(
    val date: String,
    val place: String,
)