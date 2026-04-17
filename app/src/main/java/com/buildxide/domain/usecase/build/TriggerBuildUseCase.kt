package com.buildxide.domain.usecase.build

import com.buildxide.data.repository.BuildRepository
import javax.inject.Inject

class TriggerBuildUseCase @Inject constructor(
    private val buildRepository: BuildRepository
) {
    suspend operator fun invoke(
        owner: String,
        repo: String,
        branch: String,
        buildType: String
    ) = buildRepository.triggerBuild(owner, repo, branch, buildType)
}
