package ru.myitschool.work.data.repo

import ru.myitschool.work.data.source.NetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import ru.myitschool.work.domain.auth.entities.BookingEntity
import ru.myitschool.work.domain.auth.entities.UserEntity
import kotlinx.serialization.json.Json

object MainRepository {

    @Serializable
    data class BookingDto(
        val roomName: String,
        val time: String
    )

    @Serializable
    data class UserDto(
        val name: String,
        val photoUrl: String,
        val bookingList: List<BookingDto>
    )

    private val json = Json { ignoreUnknownKeys = true }

    private var codeCache: String? = null


    private var cachedUser: UserEntity? = null

    suspend fun getUserInfo(code: String): Result<UserEntity> {
        return withContext(Dispatchers.IO) {
            runCatching {
                if (codeCache == code && cachedUser != null) return@runCatching cachedUser!!

                codeCache = code
                val isAuth = NetworkDataSource.checkAuth(code).getOrThrow()
                if (!isAuth) throw Exception("Авторизация не пройдена")

                val userJson = NetworkDataSource.getUserData(code).getOrThrow()
                val userResponse = json.decodeFromString<UserDto>(userJson)

                val user = UserEntity(
                    name = userResponse.name,
                    photoUrl = userResponse.photoUrl,
                    bookingList = userResponse.bookingList.map { BookingEntity(it.roomName, it.time) }
                )

                cachedUser = user
                user
            }
        }
    }
}