package com.buildxide.data.remote.model

import com.google.gson.annotations.SerializedName

data class FileContentResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("path")
    val path: String,

    @SerializedName("sha")
    val sha: String,

    @SerializedName("size")
    val size: Long,

    @SerializedName("type")
    val type: String, // "file"

    @SerializedName("content")
    val content: String?, // Base64 encoded

    @SerializedName("encoding")
    val encoding: String?, // "base64"

    @SerializedName("html_url")
    val htmlUrl: String?,

    @SerializedName("download_url")
    val downloadUrl: String?
)

data class FolderContentResponse(
    @SerializedName("name")
    val name: String,

    @SerializedName("path")
    val path: String,

    @SerializedName("sha")
    val sha: String,

    @SerializedName("size")
    val size: Long,

    @SerializedName("type")
    val type: String, // "file" or "dir"

    @SerializedName("html_url")
    val htmlUrl: String?,

    @SerializedName("download_url")
    val downloadUrl: String?
)

data class UpdateFileRequest(
    @SerializedName("message")
    val message: String,

    @SerializedName("content")
    val content: String, // Base64 encoded

    @SerializedName("sha")
    val sha: String? = null,

    @SerializedName("branch")
    val branch: String? = null
)

data class UpdateFileResponse(
    @SerializedName("content")
    val content: FileContentResponse?,

    @SerializedName("commit")
    val commit: CommitInfo?
)

data class CommitInfo(
    @SerializedName("sha")
    val sha: String,

    @SerializedName("message")
    val message: String
)
