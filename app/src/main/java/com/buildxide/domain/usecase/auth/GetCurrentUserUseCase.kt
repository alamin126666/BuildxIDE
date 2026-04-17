package com.buildxide.domain.usecase.auth

import com.buildxide.data.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() = authRepository.getCurrentUser()

    fun isLoggedIn() = authRepository.isLoggedIn()

    fun logout() = authRepository.logout()
}
