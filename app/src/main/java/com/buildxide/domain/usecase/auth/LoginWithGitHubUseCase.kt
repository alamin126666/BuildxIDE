package com.buildxide.domain.usecase.auth

import com.buildxide.data.repository.AuthRepository
import javax.inject.Inject

class LoginWithGitHubUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(code: String) = authRepository.exchangeCodeForToken(code)
}
