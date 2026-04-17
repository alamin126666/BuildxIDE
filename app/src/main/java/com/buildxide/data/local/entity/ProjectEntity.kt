package com.buildxide.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    indices = [Index(value = ["repo_full_name"], unique = true)]
)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val repoName: String,
    val repoFullName: String,
    val owner: String,
    val description: String? = null,
    val defaultBranch: String = "main",
    val lastOpened: Long? = null,
    val isFavorite: Boolean = false,
    val localPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
