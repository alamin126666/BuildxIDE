package com.buildxide.data.remote.model

import com.google.gson.annotations.SerializedName

data class GitHubRepoResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("full_name")
    val fullName: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("private")
    val isPrivate: Boolean,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("clone_url")
    val cloneUrl: String,

    @SerializedName("default_branch")
    val defaultBranch: String,

    @SerializedName("owner")
    val owner: RepoOwner,

    @SerializedName("updated_at")
    val updatedAt: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("language")
    val language: String?,

    @SerializedName("stargazers_count")
    val stars: Int,

    @SerializedName("forks_count")
    val forks: Int
)

data class RepoOwner(
    @SerializedName("login")
    val login: String,

    @SerializedName("id")
    val id: Long,

    @SerializedName("avatar_url")
    val avatarUrl: String?
)
