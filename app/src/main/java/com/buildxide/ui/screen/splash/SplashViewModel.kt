package com.buildxide.ui.screen.splash

import androidx.lifecycle.ViewModel
import com.buildxide.domain.usecase.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    fun isLoggedIn(): Boolean = getCurrentUserUseCase.isLoggedIn()
}
