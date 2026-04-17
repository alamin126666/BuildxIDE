package com.buildxide.domain.model

data class BuildRun(
    val id: Long,
    val runId: String,
    val projectId: Long,
    val status: BuildStatus,
    val conclusion: BuildConclusion?,
    val buildType: BuildType,
    val startedAt: Long?,
    val finishedAt: Long?,
    val apkUrl: String?,
    val logUrl: String?
)

enum class BuildStatus {
    QUEUED, IN_PROGRESS, COMPLETED, UNKNOWN
}

enum class BuildConclusion {
    SUCCESS, FAILURE, CANCELLED, SKIPPED, UNKNOWN
}

enum class BuildType {
    DEBUG, RELEASE
}
