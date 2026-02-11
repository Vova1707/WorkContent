package ru.myitschool.work.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.myitschool.work.data.repo.AuthRepository
import ru.myitschool.work.domain.auth.CheckAndSaveAuthCodeUseCase

class AuthViewModel : ViewModel() {
    private val checkAndSaveAuthCodeUseCase by lazy { CheckAndSaveAuthCodeUseCase(AuthRepository) }
    private val _uiState = MutableStateFlow<AuthState>(AuthState.Data())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    private val _actionFlow = MutableSharedFlow<String>()
    val actionFlow: SharedFlow<String> = _actionFlow

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Send -> {
                val code = intent.text
                viewModelScope.launch(Dispatchers.Default) {
                    _uiState.value = AuthState.Loading
                    checkAndSaveAuthCodeUseCase.invoke(code).fold(
                        onSuccess = {
                            _actionFlow.emit(intent.text)
                        },
                        onFailure = { error ->

                            val errorMsg = when {
                                error.message?.contains("401") == true -> "Неверный код"
                                else -> "Ошибка подключения"
                            }
                            _uiState.value = AuthState.Data(error = errorMsg)
                        }
                    )
                }
            }

            is AuthIntent.TextInput -> {
                if (_uiState.value is AuthState.Data) {
                    _uiState.value = AuthState.Data(error = null)
                }
            }
        }
    }
}