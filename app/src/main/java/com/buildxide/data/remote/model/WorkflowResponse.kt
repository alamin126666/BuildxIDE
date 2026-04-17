package com.buildxide.data.remote.model

import com.google.gson.annotations.SerializedName

data class WorkflowDispatchRequest(
    @SerializedName("ref")
    val ref: String,

    @SerializedName("inputs")
    val inputs: Map<String, String>? = null
)

data class WorkflowRunsResponse(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("workflow_runs")
    val workflowRuns: List<WorkflowRun>
)

data class WorkflowRun(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String?,

    @SerializedName("head_branch")
    val headBranch: String?,

    @SerializedName("head_sha")
    val headSha: String?,

    @SerializedName("path")
    val path: String?,

    @SerializedName("run_number")
    val runNumber: Int?,

    @SerializedName("event")
    val event: String?,

    @SerializedName("status")
    val status: String?, // queued, in_progress, completed

    @SerializedName("conclusion")
    val conclusion: String?, // success, failure, cancelled, skipped

    @SerializedName("workflow_id")
    val workflowId: Long?,

    @SerializedName("html_url")
    val htmlUrl: String?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?,

    @SerializedName("run_started_at")
    val runStartedAt: String?,

    @SerializedName("jobs_url")
    val jobsUrl: String?,

    @SerializedName("logs_url")
    val logsUrl: String?,

    @SerializedName("artifacts_url")
    val artifactsUrl: String?
)

data class ArtifactsResponse(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("artifacts")
    val artifacts: List<Artifact>
)

data class Artifact(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String,

    @SerializedName("size_in_bytes")
    val sizeInBytes: Long,

    @SerializedName("archive_download_url")
    val archiveDownloadUrl: String?,

    @SerializedName("expired")
    val expired: Boolean,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("expires_at")
    val expiresAt: String?
)
