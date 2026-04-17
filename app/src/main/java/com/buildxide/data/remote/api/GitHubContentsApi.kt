package com.buildxide.data.remote.api

import com.buildxide.data.remote.model.FileContentResponse
import com.buildxide.data.remote.model.FolderContentResponse
import com.buildxide.data.remote.model.UpdateFileRequest
import com.buildxide.data.remote.model.UpdateFileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubContentsApi {

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFileContent(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Query("ref") ref: String? = null
    ): Response<FileContentResponse>

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFolderContent(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String = "",
        @Query("ref") ref: String? = null
    ): Response<List<FolderContentResponse>>

    @PUT("repos/{owner}/{repo}/contents/{path}")
    suspend fun updateFile(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Body body: UpdateFileRequest
    ): Response<UpdateFileResponse>

    @GET("repos/{owner}/{repo}/contents/")
    suspend fun getRootContent(
        @Header("Authorization") token: String,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("ref") ref: String? = null
    ): Response<List<FolderContentResponse>>
}
