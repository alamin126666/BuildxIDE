package com.buildxide.data.remote.api

import com.buildxide.data.remote.model.GitHubUserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface GitHubUserApi {

    @GET("user")
    suspend fun getAuthenticatedUser(
        @Header("Authorization") token: String
    ): Response<GitHubUserResponse>
}
