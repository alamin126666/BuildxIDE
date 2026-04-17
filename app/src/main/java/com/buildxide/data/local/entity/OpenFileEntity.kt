package com.buildxide.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "open_files",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["project_id", "file_path"], unique = true)]
)
data class OpenFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val filePath: String,
    val fileName: String,
    val content: String? = null,
    val sha: String? = null,
    val isModified: Boolean = false,
    val tabIndex: Int = 0,
    val lastSaved: Long? = null
)
