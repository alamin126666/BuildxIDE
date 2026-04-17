package com.buildxide.ui.screen.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildxide.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val uiState: State<LoginUiState> = _uiState

    fun handleOAuthCallback(code: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            authRepository.exchangeCodeForToken(code)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Authentication failed")
                }
        }
    }

    fun loginWithPat(token: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            authRepository.loginWithPat(token)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "Invalid token")
                }
        }
    }
}

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
