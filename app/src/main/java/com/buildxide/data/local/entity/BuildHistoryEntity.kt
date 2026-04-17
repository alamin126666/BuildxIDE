package com.buildxide.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "build_history",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["project_id"])]
)
data class BuildHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val runId: String,
    val status: String? = null, // queued, in_progress, completed
    val conclusion: String? = null, // success, failure, cancelled
    val buildType: String = "debug", // debug, release
    val startedAt: Long? = null,
    val finishedAt: Long? = null,
    val apkUrl: String? = null,
    val logUrl: String? = null
)
