package com.buildxide.domain.model

data class EditorSettings(
    val fontSize: Int = 14,
    val fontFamily: String = "JetBrains Mono",
    val tabSize: Int = 4,
    val theme: EditorTheme = EditorTheme.DARK,
    val wordWrap: Boolean = true,
    val showLineNumbers: Boolean = true
)

enum class EditorTheme {
    DARK, LIGHT, AMOLED
}
