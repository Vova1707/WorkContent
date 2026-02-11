package ru.myitschool.work.domain.auth.entities

data class UserEntity(
    val name: String,
    val photoUrl: String,
    val bookingList: List<BookingEntity>
)