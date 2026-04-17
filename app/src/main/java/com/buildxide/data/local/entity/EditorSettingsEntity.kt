package com.buildxide.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "editor_settings")
data class EditorSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val fontSize: Int = 14,
    val fontFamily: String = "JetBrains Mono",
    val tabSize: Int = 4,
    val theme: String = "dark", // dark, light, amoled
    val wordWrap: Boolean = true,
    val showLineNumbers: Boolean = true
)
