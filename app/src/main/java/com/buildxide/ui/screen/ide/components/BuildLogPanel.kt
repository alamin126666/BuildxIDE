package com.buildxide.ui.screen.ide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.buildxide.data.repository.BuildStatus
import com.buildxide.ui.theme.AccentGreen
import com.buildxide.ui.theme.AccentRed
import com.buildxide.ui.theme.AccentYellow
import com.buildxide.ui.theme.Background
import com.buildxide.ui.theme.Border
import com.buildxide.ui.theme.StatusError
import com.buildxide.ui.theme.StatusRunning
import com.buildxide.ui.theme.StatusSuccess
import com.buildxide.ui.theme.Surface
import com.buildxide.ui.theme.SurfaceVariant
import com.buildxide.ui.theme.TextPrimary
import com.buildxide.ui.theme.TextSecondary

@Composable
fun BuildLogPanel(
    logs: List<String>,
    buildStatus: BuildStatus?,
    onClear: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Surface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "BUILD LOG",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextSecondary
                )

                buildStatus?.let { status ->
                    Spacer(modifier = Modifier.width(12.dp))

                    val statusColor = when (status.status) {
                        "completed" -> when (status.conclusion) {
                            "success" -> StatusSuccess
                            "failure" -> StatusError
                            else -> TextSecondary
                        }
                        "in_progress" -> StatusRunning
                        else -> TextSecondary
                    }

                    val statusText = when (status.status) {
                        "completed" -> status.conclusion?.uppercase() ?: "COMPLETED"
                        "in_progress" -> "BUILDING..."
                        else -> status.status.uppercase()
                    }

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = statusColor
                    )
                }
            }

            Row {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(logs.joinToString("\n")))
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = Border)

        // Log content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            items(logs) { log ->
                LogLine(log = log)
            }
        }
    }
}

@Composable
private fun LogLine(log: String) {
    val color = when {
        log.contains("error", ignoreCase = true) ||
                log.contains("failed", ignoreCase = true) -> StatusError
        log.contains("warning", ignoreCase = true) ||
                log.contains("warn", ignoreCase = true) -> AccentYellow
        log.contains("success", ignoreCase = true) ||
                log.contains("successful", ignoreCase = true) -> StatusSuccess
        log.contains("BUILD SUCCESSFUL") -> StatusSuccess
        log.contains("BUILD FAILED") -> StatusError
        log.startsWith("> Task") -> TextSecondary
        else -> TextPrimary
    }

    Text(
        text = log,
        style = MaterialTheme.typography.bodySmall.copy(
            fontFamily = FontFamily.Monospace
        ),
        color = color,
        modifier = Modifier.padding(vertical = 1.dp)
    )
}
