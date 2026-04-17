package com.buildxide.data.repository

import com.buildxide.BuildConfig
import com.buildxide.data.remote.api.GitHubAuthApi
import com.buildxide.data.remote.api.GitHubUserApi
import com.buildxide.data.remote.model.GitHubUserResponse
import com.buildxide.util.SecureTokenStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val secureTokenStorage: SecureTokenStorage,
    private val gitHubAuthApi: GitHubAuthApi,
    private val gitHubUserApi: GitHubUserApi
) {

    fun isLoggedIn(): Boolean = secureTokenStorage.isLoggedIn()

    fun getToken(): String? = secureTokenStorage.getToken()

    fun getAuthHeader(): String? {
        val token = secureTokenStorage.getToken()
        return token?.let { "Bearer $it" }
    }

    suspend fun exchangeCodeForToken(code: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = gitHubAuthApi.exchangeCodeForToken(
                clientId = BuildConfig.GITHUB_CLIENT_ID,
                clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
                code = code,
                redirectUri = BuildConfig.GITHUB_REDIRECT_URI
            )

            if (response.isSuccessful) {
                val tokenResponse = response.body()
                val token = tokenResponse?.accessToken
                if (token != null) {
                    secureTokenStorage.saveToken(token)
                    Result.success(token)
                } else {
                    Result.failure(Exception(tokenResponse?.errorDescription ?: "Failed to get token"))
                }
            } else {
                Result.failure(Exception("Token exchange failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loginWithPat(token: String): Result<GitHubUserResponse> = withContext(Dispatchers.IO) {
        try {
            val response = gitHubUserApi.getAuthenticatedUser("Bearer $token")
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    secureTokenStorage.saveToken(token)
                    Result.success(user)
                } else {
                    Result.failure(Exception("Invalid response"))
                }
            } else {
                Result.failure(Exception("Invalid token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): Result<GitHubUserResponse> = withContext(Dispatchers.IO) {
        try {
            val token = getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val response = gitHubUserApi.getAuthenticatedUser(token)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get user: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        secureTokenStorage.clearToken()
    }
}
