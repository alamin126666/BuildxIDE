package com.buildxide.data.remote.model

import com.google.gson.annotations.SerializedName

data class GitHubUserResponse(
    @SerializedName("login")
    val login: String,

    @SerializedName("id")
    val id: Long,

    @SerializedName("avatar_url")
    val avatarUrl: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("bio")
    val bio: String?,

    @SerializedName("public_repos")
    val publicRepos: Int,

    @SerializedName("private_repos")
    val privateRepos: Int?,

    @SerializedName("total_private_repos")
    val totalPrivateRepos: Int?
)
