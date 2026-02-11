package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.MainRepository
import ru.myitschool.work.domain.auth.entities.UserEntity

class GetUserInfoUseCase(
    private val repository: MainRepository = MainRepository
) {
    suspend operator fun invoke(code: String): Result<UserEntity> {
        return repository.getUserInfo(code)
    }
}