package com.buildxide.domain.model

data class Project(
    val id: Long,
    val repoName: String,
    val repoFullName: String,
    val owner: String,
    val description: String?,
    val defaultBranch: String,
    val lastOpened: Long?,
    val isFavorite: Boolean,
    val avatarUrl: String? = null
)
