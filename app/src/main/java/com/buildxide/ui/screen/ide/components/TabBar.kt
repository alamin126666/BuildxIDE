package com.buildxide.ui.screen.ide.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
nimport androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.buildxide.data.local.entity.OpenFileEntity
import com.buildxide.ui.theme.AccentBlue
import com.buildxide.ui.theme.Background
import com.buildxide.ui.theme.Surface
import com.buildxide.ui.theme.SurfaceVariant
import com.buildxide.ui.theme.TextPrimary
import com.buildxide.ui.theme.TextSecondary

@Composable
fun TabBar(
    files: List<OpenFileEntity>,
    selectedFile: OpenFileEntity?,
    onFileSelect: (OpenFileEntity) -> Unit,
    onFileClose: (OpenFileEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Background)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        files.forEach { file ->
            val isSelected = file.id == selectedFile?.id

            TabItem(
                file = file,
                isSelected = isSelected,
                onClick = { onFileSelect(file) },
                onClose = { onFileClose(file) }
            )

            Spacer(modifier = Modifier.width(2.dp))
        }
    }
}

@Composable
private fun TabItem(
    file: OpenFileEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    val backgroundColor = if (isSelected) Surface else SurfaceVariant.copy(alpha = 0.5f)
    val textColor = if (isSelected) TextPrimary else TextSecondary

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Modified indicator
        if (file.isModified) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(AccentBlue)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        // File name
        Text(
            text = file.fileName,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 120.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        // Close button
        IconButton(
            onClick = onClose,
            modifier = Modifier.size(18.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
