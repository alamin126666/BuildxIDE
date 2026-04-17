package com.buildxide.data.repository

import com.buildxide.data.local.dao.ProjectDao
import com.buildxide.data.local.entity.ProjectEntity
import com.buildxide.data.remote.api.GitHubRepoApi
import com.buildxide.data.remote.model.GitHubRepoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val authRepository: AuthRepository,
    private val gitHubRepoApi: GitHubRepoApi
) {

    fun getLocalProjects(): Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    suspend fun syncGitHubRepos(): Result<List<GitHubRepoResponse>> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val response = gitHubRepoApi.getUserRepos(token)
            if (response.isSuccessful) {
                val repos = response.body() ?: emptyList()

                // Sync with local database
                repos.forEach { repo ->
                    val existingProject = projectDao.getProjectByRepoFullName(repo.fullName)
                    if (existingProject == null) {
                        projectDao.insertProject(
                            ProjectEntity(
                                repoName = repo.name,
                                repoFullName = repo.fullName,
                                owner = repo.owner.login,
                                description = repo.description,
                                defaultBranch = repo.defaultBranch
                            )
                        )
                    }
                }

                Result.success(repos)
            } else {
                Result.failure(Exception("Failed to fetch repos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProjectById(id: Long): ProjectEntity? = withContext(Dispatchers.IO) {
        projectDao.getProjectById(id)
    }

    suspend fun updateLastOpened(projectId: Long) = withContext(Dispatchers.IO) {
        projectDao.updateLastOpened(projectId)
    }

    suspend fun toggleFavorite(projectId: Long) = withContext(Dispatchers.IO) {
        val project = projectDao.getProjectById(projectId)
        project?.let {
            projectDao.updateFavoriteStatus(projectId, !it.isFavorite)
        }
    }

    suspend fun deleteProject(projectId: Long) = withContext(Dispatchers.IO) {
        projectDao.deleteProjectById(projectId)
    }

    suspend fun searchProjects(query: String): List<ProjectEntity> = withContext(Dispatchers.IO) {
        val allProjects = projectDao.getAllProjects().first()
        allProjects.filter {
            it.repoName.contains(query, ignoreCase = true) ||
                    it.description?.contains(query, ignoreCase = true) == true
        }
    }
}
