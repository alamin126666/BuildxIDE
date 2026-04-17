package com.buildxide.domain.model

data class GitHubUser(
    val login: String,
    val id: Long,
    val avatarUrl: String?,
    val name: String?,
    val email: String?,
    val bio: String?,
    val publicRepos: Int,
    val privateRepos: Int?
)
