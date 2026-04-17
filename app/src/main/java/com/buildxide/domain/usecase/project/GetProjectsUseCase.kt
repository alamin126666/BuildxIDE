package com.buildxide.domain.usecase.project

import com.buildxide.data.repository.ProjectRepository
import javax.inject.Inject

class GetProjectsUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke() = projectRepository.getLocalProjects()

    suspend fun syncFromGitHub() = projectRepository.syncGitHubRepos()

    suspend fun search(query: String) = projectRepository.searchProjects(query)
}
