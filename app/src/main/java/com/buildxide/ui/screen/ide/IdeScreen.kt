package com.buildxide.ui.screen.ide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buildxide.domain.model.FileNode
import com.buildxide.domain.model.FileType
import com.buildxide.ui.screen.ide.components.BuildLogPanel
import com.buildxide.ui.screen.ide.components.CodeEditor
import com.buildxide.ui.screen.ide.components.TabBar
import com.buildxide.ui.theme.AccentBlue
import com.buildxide.ui.theme.AccentGreen
import com.buildxide.ui.theme.AccentOrange
import com.buildxide.ui.theme.AccentPurple
import com.buildxide.ui.theme.AccentYellow
import com.buildxide.ui.theme.Background
import com.buildxide.ui.theme.Border
import com.buildxide.ui.theme.FileFolder
import com.buildxide.ui.theme.FileGradle
import com.buildxide.ui.theme.FileJson
import com.buildxide.ui.theme.FileKotlin
import com.buildxide.ui.theme.FileXml
import com.buildxide.ui.theme.FileYaml
import com.buildxide.ui.theme.Surface
import com.buildxide.ui.theme.SurfaceVariant
import com.buildxide.ui.theme.TextPrimary
import com.buildxide.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IdeScreen(
    projectId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: IdeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val openFiles by viewModel.openFiles.collectAsState()
    val selectedFile by viewModel.selectedFile
    val fileContent by viewModel.fileContent
    val isLoading by viewModel.isLoading
    val showBuildPanel by viewModel.showBuildPanel
    val snackbarHostState = remember { SnackbarHostState() }

    var showFileTree by remember { mutableStateOf(true) }
    var showMenu by remember { mutableStateOf(false) }

    LaunchedEffect(projectId) {
        viewModel.loadProject(projectId)
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is IdeUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as IdeUiState.Error).message)
            }
            is IdeUiState.FileSaved -> {
                snackbarHostState.showSnackbar("File saved")
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.projectName.value,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextSecondary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.saveCurrentFile() },
                        enabled = selectedFile != null
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save",
                            tint = if (selectedFile != null) AccentBlue else TextSecondary.copy(alpha = 0.5f)
                        )
                    }

                    IconButton(onClick = { viewModel.toggleBuildPanel() }) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Build",
                            tint = AccentGreen
                        )
                    }

                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = TextSecondary
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Settings", color = TextPrimary) },
                            onClick = {
                                showMenu = false
                                onNavigateToSettings()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = TextSecondary
                                )
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = TextPrimary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Background)
        ) {
            // Tab Bar
            if (openFiles.isNotEmpty()) {
                TabBar(
                    files = openFiles,
                    selectedFile = selectedFile,
                    onFileSelect = { viewModel.selectFile(it) },
                    onFileClose = { viewModel.closeFile(it) }
                )
            }

            // Main Content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // File Tree Panel
                AnimatedVisibility(visible = showFileTree) {
                    FileTreePanel(
                        rootNodes = viewModel.fileTree.value,
                        selectedPath = selectedFile?.filePath,
                        onFileClick = { fileNode ->
                            viewModel.openFile(fileNode)
                        },
                        onFolderClick = { folderNode ->
                            viewModel.toggleFolder(folderNode)
                        },
                        onRefresh = { viewModel.refreshFileTree() },
                        isLoading = isLoading,
                        modifier = Modifier.width(280.dp)
                    )
                }

                // Code Editor
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Background)
                ) {
                    if (selectedFile != null) {
                        CodeEditor(
                            content = fileContent,
                            onContentChange = { viewModel.updateFileContent(it) },
                            fileName = selectedFile.fileName,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        EmptyEditorState()
                    }
                }
            }

            // Build Log Panel
            AnimatedVisibility(visible = showBuildPanel) {
                BuildLogPanel(
                    logs = viewModel.buildLogs.value,
                    buildStatus = viewModel.buildStatus.value,
                    onClear = { viewModel.clearBuildLogs() },
                    onClose = { viewModel.toggleBuildPanel() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

@Composable
fun FileTreePanel(
    rootNodes: List<FileNode>,
    selectedPath: String?,
    onFileClick: (FileNode) -> Unit,
    onFolderClick: (FileNode) -> Unit,
    onRefresh: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(Surface)
            .padding(8.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FILES",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onRefresh, modifier = Modifier.size(24.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = AccentBlue,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Refresh",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(color = Border)

        Spacer(modifier = Modifier.height(8.dp))

        // File Tree
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(rootNodes) { node ->
                FileTreeItem(
                    node = node,
                    selectedPath = selectedPath,
                    onFileClick = onFileClick,
                    onFolderClick = onFolderClick,
                    depth = 0
                )
            }
        }
    }
}

@Composable
fun FileTreeItem(
    node: FileNode,
    selectedPath: String?,
    onFileClick: (FileNode) -> Unit,
    onFolderClick: (FileNode) -> Unit,
    depth: Int
) {
    val isSelected = node.path == selectedPath
    val indent = depth * 16

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = indent.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) AccentBlue.copy(alpha = 0.2f) else Color.Transparent)
            .clickable {
                if (node.isDirectory) onFolderClick(node)
                else onFileClick(node)
            }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Folder/File Icon
        Icon(
            imageVector = when {
                node.isDirectory -> if (node.isExpanded) Icons.Default.FolderOpen else Icons.Default.Folder
                else -> getFileIcon(node.name)
            },
            contentDescription = null,
            tint = when {
                node.isDirectory -> FileFolder
                else -> getFileColor(node.fileType)
            },
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = node.name,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = if (isSelected) AccentBlue else TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    // Render children if expanded
    if (node.isDirectory && node.isExpanded) {
        node.children.forEach { childNode ->
            FileTreeItem(
                node = childNode,
                selectedPath = selectedPath,
                onFileClick = onFileClick,
                onFolderClick = onFolderClick,
                depth = depth + 1
            )
        }
    }
}

@Composable
fun EmptyEditorState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = TextSecondary.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select a file to start editing",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun getFileIcon(fileName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (fileName.substringAfterLast('.', "").lowercase()) {
        "kt" -> Icons.Default.Build
        "java" -> Icons.Default.Build
        "xml" -> Icons.Default.Build
        "json" -> Icons.Default.Build
        else -> Icons.Default.Build
    }
}

private fun getFileColor(fileType: FileType): Color {
    return when (fileType) {
        FileType.KOTLIN -> FileKotlin
        FileType.JAVA -> AccentYellow
        FileType.XML -> FileXml
        FileType.GRADLE -> FileGradle
        FileType.JSON -> FileJson
        FileType.YAML -> FileYaml
        FileType.MARKDOWN -> AccentPurple
        else -> TextSecondary
    }
}
