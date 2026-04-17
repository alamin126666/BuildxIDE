package com.buildxide.ui.screen.ide.components

import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import io.github.rosemoe.sora.event.ContentChangeEvent
import io.github.rosemoe.sora.langs.java.JavaLanguage
import io.github.rosemoe.sora.langs.textmate.TextMateLanguage
import io.github.rosemoe.sora.widget.CodeEditor
import io.github.rosemoe.sora.widget.component.EditorAutoCompletion
import io.github.rosemoe.sora.widget.schemes.EditorColorScheme
import com.buildxide.ui.theme.Background
import com.buildxide.ui.theme.SyntaxComment
import com.buildxide.ui.theme.SyntaxFunction
import com.buildxide.ui.theme.SyntaxKeyword
import com.buildxide.ui.theme.SyntaxNumber
import com.buildxide.ui.theme.SyntaxOperator
import com.buildxide.ui.theme.SyntaxString
import com.buildxide.ui.theme.SyntaxType
import com.buildxide.ui.theme.TextPrimary
import com.buildxide.ui.theme.TextSecondary

@Composable
fun CodeEditor(
    content: String,
    onContentChange: (String) -> Unit,
    fileName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language = remember(fileName) { getLanguageForFile(fileName) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        AndroidView(
            factory = { ctx ->
                CodeEditor(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // Set language
                    setEditorLanguage(language)

                    // Set color scheme
                    colorScheme = createGitHubDarkColorScheme()

                    // Set text
                    setText(content)

                    // Configure editor
                    isLineNumberEnabled = true
                    isWordwrap = true
                    getComponent(EditorAutoCompletion::class.java).isEnabled = true

                    // Subscribe to content changes
                    subscribeEvent(ContentChangeEvent::class.java) { _, _ ->
                        onContentChange(text.toString())
                    }
                }
            },
            update = { editor ->
                if (editor.text.toString() != content) {
                    editor.setText(content)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

private fun getLanguageForFile(fileName: String): io.github.rosemoe.sora.lang.Language {
    return when (fileName.substringAfterLast('.', "").lowercase()) {
        "kt", "java" -> JavaLanguage()
        "xml" -> try {
            TextMateLanguage.create("text.xml", true)
        } catch (e: Exception) {
            io.github.rosemoe.sora.langs.empty.EmptyLanguage()
        }
        "json" -> try {
            TextMateLanguage.create("source.json", true)
        } catch (e: Exception) {
            io.github.rosemoe.sora.langs.empty.EmptyLanguage()
        }
        else -> io.github.rosemoe.sora.langs.empty.EmptyLanguage()
    }
}

private fun createGitHubDarkColorScheme(): EditorColorScheme {
    return EditorColorScheme().apply {
        // Base colors
        setColor(EditorColorScheme.WHOLE_BACKGROUND, Background.value.toInt())
        setColor(EditorColorScheme.LINE_NUMBER_BACKGROUND, Background.value.toInt())
        setColor(EditorColorScheme.LINE_NUMBER, TextSecondary.value.toInt())
        setColor(EditorColorScheme.LINE_NUMBER_CURRENT, TextPrimary.value.toInt())
        setColor(EditorColorScheme.TEXT_NORMAL, TextPrimary.value.toInt())
        setColor(EditorColorScheme.SELECTION_HANDLE, SyntaxKeyword.value.toInt())
        setColor(EditorColorScheme.SELECTION_INSERT, SyntaxKeyword.value.toInt())
        setColor(EditorColorScheme.HIGHLIGHTED_DELIMITERS, SyntaxKeyword.value.toInt())

        // Syntax colors
        setColor(EditorColorScheme.OPERATOR, SyntaxOperator.value.toInt())
        setColor(EditorColorScheme.BLOCK_LINE, TextSecondary.value.toInt())
        setColor(EditorColorScheme.BLOCK_LINE_CURRENT, TextPrimary.value.toInt())

        // For Java/Kotlin
        setColor(EditorColorScheme.COMMENT, SyntaxComment.value.toInt())
        setColor(EditorColorScheme.STRING, SyntaxString.value.toInt())
        setColor(EditorColorScheme.KEYWORD, SyntaxKeyword.value.toInt())
        setColor(EditorColorScheme.FUNCTION_NAME, SyntaxFunction.value.toInt())
        setColor(EditorColorScheme.IDENTIFIER_NAME, TextPrimary.value.toInt())
        setColor(EditorColorScheme.LITERAL, SyntaxNumber.value.toInt())
        setColor(EditorColorScheme.TYPE_NAME, SyntaxType.value.toInt())
    }
}
