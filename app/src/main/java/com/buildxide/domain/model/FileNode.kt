package com.buildxide.domain.model

data class FileNode(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val depth: Int = 0,
    val isExpanded: Boolean = false,
    val children: List<FileNode> = emptyList(),
    val sha: String? = null,
    val size: Long = 0
) {
    val extension: String
        get() = name.substringAfterLast('.', "")

    val isImage: Boolean
        get() = extension.lowercase() in listOf("png", "jpg", "jpeg", "gif", "webp", "svg")

    val fileType: FileType
        get() = when (extension.lowercase()) {
            "kt" -> FileType.KOTLIN
            "java" -> FileType.JAVA
            "xml" -> FileType.XML
            "gradle", "kts" -> FileType.GRADLE
            "json" -> FileType.JSON
            "yml", "yaml" -> FileType.YAML
            "md" -> FileType.MARKDOWN
            "txt" -> FileType.TEXT
            "png", "jpg", "jpeg", "gif", "webp" -> FileType.IMAGE
            else -> FileType.UNKNOWN
        }
}

enum class FileType {
    KOTLIN, JAVA, XML, GRADLE, JSON, YAML, MARKDOWN, TEXT, IMAGE, UNKNOWN
}
