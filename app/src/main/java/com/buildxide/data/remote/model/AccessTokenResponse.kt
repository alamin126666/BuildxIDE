package com.buildxide.data.remote.model

import com.google.gson.annotations.SerializedName

data class AccessTokenResponse(
    @SerializedName("access_token")
    val accessToken: String?,

    @SerializedName("token_type")
    val tokenType: String?,

    @SerializedName("scope")
    val scope: String?,

    @SerializedName("error")
    val error: String?,

    @SerializedName("error_description")
    val errorDescription: String?
)
