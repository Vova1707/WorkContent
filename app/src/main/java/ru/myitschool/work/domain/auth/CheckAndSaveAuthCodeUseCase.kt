package ru.myitschool.work.domain.auth

import ru.myitschool.work.data.repo.AuthRepository

class CheckAndSaveAuthCodeUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(
        text: String
    ): Result<Unit> {

        val isValid = text.length == 4 && text.all {it.isLetterOrDigit()}
        if (!isValid) return Result.failure(IllegalArgumentException("Неверный формат кода!"))

        return repository.checkAndSave(text).mapCatching { success ->
            if (!success) error("Code is incorrect")
        }
    }
}