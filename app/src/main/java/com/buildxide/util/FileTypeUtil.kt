package com.buildxide.util

import com.buildxide.domain.model.FileType

object FileTypeUtil {

    fun getFileType(fileName: String): FileType {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return when (extension) {
            "kt" -> FileType.KOTLIN
            "java" -> FileType.JAVA
            "xml" -> FileType.XML
            "gradle", "kts" -> FileType.GRADLE
            "json" -> FileType.JSON
            "yml", "yaml" -> FileType.YAML
            "md" -> FileType.MARKDOWN
            "txt" -> FileType.TEXT
            "png", "jpg", "jpeg", "gif", "webp", "svg" -> FileType.IMAGE
            else -> FileType.UNKNOWN
        }
    }

    fun getFileIconResource(fileType: FileType): Int {
        // Return drawable resource IDs for file icons
        return when (fileType) {
            FileType.KOTLIN -> android.R.drawable.ic_menu_edit
            FileType.JAVA -> android.R.drawable.ic_menu_edit
            FileType.XML -> android.R.drawable.ic_menu_agenda
            FileType.GRADLE -> android.R.drawable.ic_menu_preferences
            FileType.JSON -> android.R.drawable.ic_menu_sort_by_size
            FileType.YAML -> android.R.drawable.ic_menu_info_details
            FileType.MARKDOWN -> android.R.drawable.ic_menu_help
            FileType.TEXT -> android.R.drawable.ic_menu_edit
            FileType.IMAGE -> android.R.drawable.ic_menu_gallery
            FileType.UNKNOWN -> android.R.drawable.ic_menu_close_clear_cancel
        }
    }

    fun isCodeFile(fileName: String): Boolean {
        val codeExtensions = listOf(
            "kt", "java", "xml", "gradle", "kts", "json", "yml", "yaml",
            "md", "txt", "properties", "pro", "cfg", "ini"
        )
        return codeExtensions.contains(fileName.substringAfterLast('.', "").lowercase())
    }

    fun isImageFile(fileName: String): Boolean {
        val imageExtensions = listOf("png", "jpg", "jpeg", "gif", "webp", "svg", "bmp")
        return imageExtensions.contains(fileName.substringAfterLast('.', "").lowercase())
    }

    fun isBinaryFile(fileName: String): Boolean {
        val binaryExtensions = listOf(
            "apk", "aar", "jar", "so", "dll", "exe", "bin",
            "dat", "db", "sqlite", "class"
        )
        return binaryExtensions.contains(fileName.substringAfterLast('.', "").lowercase())
    }
}
