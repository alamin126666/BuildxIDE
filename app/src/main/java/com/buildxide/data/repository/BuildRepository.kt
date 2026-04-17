package com.buildxide.data.repository

import com.buildxide.data.local.dao.BuildHistoryDao
import com.buildxide.data.local.entity.BuildHistoryEntity
import com.buildxide.data.remote.api.GitHubActionsApi
import com.buildxide.data.remote.model.WorkflowDispatchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BuildRepository @Inject constructor(
    private val buildHistoryDao: BuildHistoryDao,
    private val authRepository: AuthRepository,
    private val gitHubActionsApi: GitHubActionsApi
) {

    fun getBuildHistory(projectId: Long): Flow<List<BuildHistoryEntity>> {
        return buildHistoryDao.getBuildHistoryByProject(projectId)
    }

    suspend fun triggerBuild(
        owner: String,
        repo: String,
        branch: String,
        buildType: String
    ): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            // Trigger the workflow
            val response = gitHubActionsApi.triggerWorkflow(
                token = token,
                owner = owner,
                repo = repo,
                workflowId = "build.yml",
                body = WorkflowDispatchRequest(
                    ref = branch,
                    inputs = mapOf("build_type" to buildType)
                )
            )

            if (response.isSuccessful) {
                // Wait a moment and get the run ID
                delay(3000)
                val runsResponse = gitHubActionsApi.getWorkflowRuns(token, owner, repo, 1)
                val latestRun = runsResponse.body()?.workflowRuns?.firstOrNull()

                if (latestRun != null) {
                    Result.success(latestRun.id)
                } else {
                    Result.failure(Exception("Could not get run ID"))
                }
            } else {
                Result.failure(Exception("Failed to trigger build: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun watchBuildStatus(
        owner: String,
        repo: String,
        runId: Long
    ): Flow<BuildStatus> = flow {
        val token = authRepository.getAuthHeader() ?: return@flow

        while (true) {
            try {
                val response = gitHubActionsApi.getWorkflowRun(token, owner, repo, runId)
                val run = response.body()

                if (run != null) {
                    emit(
                        BuildStatus(
                            runId = run.id,
                            status = run.status,
                            conclusion = run.conclusion,
                            runNumber = run.runNumber
                        )
                    )

                    // Stop polling if build is done
                    if (run.status == "completed") break
                }
            } catch (e: Exception) {
                // Continue polling on error
            }

            delay(5000) // Poll every 5 seconds
        }
    }

    suspend fun getBuildArtifacts(
        owner: String,
        repo: String,
        runId: Long
    ): Result<List<com.buildxide.data.remote.model.Artifact>> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val response = gitHubActionsApi.getRunArtifacts(token, owner, repo, runId)
            if (response.isSuccessful) {
                Result.success(response.body()?.artifacts ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get artifacts: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun cancelBuild(
        owner: String,
        repo: String,
        runId: Long
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = authRepository.getAuthHeader()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val response = gitHubActionsApi.cancelWorkflowRun(token, owner, repo, runId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to cancel build: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveBuildHistory(buildHistory: BuildHistoryEntity): Long = withContext(Dispatchers.IO) {
        buildHistoryDao.insertBuildHistory(buildHistory)
    }

    suspend fun updateBuildStatus(
        runId: String,
        status: String?,
        conclusion: String?,
        finishedAt: Long?
    ) = withContext(Dispatchers.IO) {
        buildHistoryDao.updateBuildStatus(runId, status, conclusion, finishedAt)
    }

    suspend fun updateApkUrl(runId: String, apkUrl: String?) = withContext(Dispatchers.IO) {
        buildHistoryDao.updateApkUrl(runId, apkUrl)
    }
}

data class BuildStatus(
    val runId: Long,
    val status: String?, // queued, in_progress, completed
    val conclusion: String?, // success, failure, cancelled
    val runNumber: Int?
)
