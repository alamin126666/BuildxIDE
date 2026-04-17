package com.buildxide.data.repository

import android.util.Base64
import com.buildxide.data.local.dao.OpenFileDao
import com.buildxide.data.local.entity.OpenFileEntity
import com.buildxide.data.remote.api.GitHubContentsApi
import com.buildxide.data.remote.model.FileContentResponse
import com.buildxide.data.remote.model.FolderContentResponse
import com.buildxide.data.remote.model.UpdateFileRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileRepository @Inject constructor(
    private val openFileDao: OpenFileDao,
    private val authRepository: AuthRepository,
    private val gitHubContentsApi: GitHubContentsApi
) {

    fun getOpenFiles(projectId: Long): Flow<List<OpenFileEntity>> {
        return openFileDao.getOpenFilesByProject(projectId)
    }

    suspend fun getFileContent(
        owner: String,
        repo: String,
        path: String,
        branch: String? = null
    ): Result<FileContentResponse> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val response = gitHubContentsApi.getFileContent(token, owner, repo, path, branch)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("Failed to get file: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFolderContent(
        owner: String,
        repo: String,
        path: String = "",
        branch: String? = null
    ): Result<List<FolderContentResponse>> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val response = if (path.isEmpty()) {
                gitHubContentsApi.getRootContent(token, owner, repo, branch)
            } else {
                gitHubContentsApi.getFolderContent(token, owner, repo, path, branch)
            }

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get folder contents: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveFile(
        owner: String,
        repo: String,
        path: String,
        content: String,
        commitMessage: String,
        branch: String,
        sha: String? = null
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            // Get current file SHA if not provided
            val currentSha = sha ?: try {
                val fileResponse = gitHubContentsApi.getFileContent(token, owner, repo, path, branch)
                fileResponse.body()?.sha
            } catch (e: Exception) {
                null // File doesn't exist yet
            }

            // Encode content to Base64
            val encodedContent = Base64.encodeToString(
                content.toByteArray(Charsets.UTF_8),
                Base64.NO_WRAP
            )

            // Create or update file
            val body = UpdateFileRequest(
                message = commitMessage,
                content = encodedContent,
                sha = currentSha,
                branch = branch
            )

            val response = gitHubContentsApi.updateFile(token, owner, repo, path, body)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to save file: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun openFile(
        projectId: Long,
        filePath: String,
        fileName: String,
        content: String?,
        sha: String?
    ): Long = withContext(Dispatchers.IO) {
        val existingFile = openFileDao.getOpenFileByPath(projectId, filePath)
        if (existingFile != null) {
            existingFile.id
        } else {
            val maxIndex = openFileDao.getMaxTabIndex(projectId) ?: -1
            val newFile = OpenFileEntity(
                projectId = projectId,
                filePath = filePath,
                fileName = fileName,
                content = content,
                sha = sha,
                tabIndex = maxIndex + 1
            )
            openFileDao.insertOpenFile(newFile)
        }
    }

    suspend fun updateFileContent(
        fileId: Long,
        content: String?,
        isModified: Boolean
    ) = withContext(Dispatchers.IO) {
        openFileDao.updateContent(fileId, content, isModified)
    }

    suspend fun closeFile(fileId: Long) = withContext(Dispatchers.IO) {
        openFileDao.deleteOpenFileById(fileId)
    }

    suspend fun closeAllFiles(projectId: Long) = withContext(Dispatchers.IO) {
        openFileDao.deleteAllOpenFilesForProject(projectId)
    }

    fun decodeBase64Content(base64Content: String): String {
        return String(Base64.decode(base64Content, Base64.DEFAULT), Charsets.UTF_8)
    }
}
