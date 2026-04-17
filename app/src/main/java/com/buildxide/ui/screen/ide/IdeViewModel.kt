package com.buildxide.ui.screen.ide

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildxide.data.local.entity.OpenFileEntity
import com.buildxide.data.local.entity.ProjectEntity
import com.buildxide.data.repository.BuildRepository
import com.buildxide.data.repository.FileRepository
import com.buildxide.data.repository.ProjectRepository
import com.buildxide.domain.model.FileNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IdeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val fileRepository: FileRepository,
    private val buildRepository: BuildRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<IdeUiState>(IdeUiState.Idle)
    val uiState: StateFlow<IdeUiState> = _uiState

    private val _project = mutableStateOf<ProjectEntity?>(null)
    val projectName = mutableStateOf("")

    private val _fileTree = mutableStateOf<List<FileNode>>(emptyList())
    val fileTree: State<List<FileNode>> = _fileTree

    private val _openFiles = MutableStateFlow<List<OpenFileEntity>>(emptyList())
    val openFiles: StateFlow<List<OpenFileEntity>> = _openFiles

    private val _selectedFile = mutableStateOf<OpenFileEntity?>(null)
    val selectedFile: State<OpenFileEntity?> = _selectedFile

    private val _fileContent = mutableStateOf("")
    val fileContent: State<String> = _fileContent

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _showBuildPanel = mutableStateOf(false)
    val showBuildPanel: State<Boolean> = _showBuildPanel

    private val _buildLogs = mutableStateOf<List<String>>(emptyList())
    val buildLogs: State<List<String>> = _buildLogs

    private val _buildStatus = mutableStateOf<BuildStatus?>(null)
    val buildStatus: State<BuildStatus?> = _buildStatus

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _project.value = projectRepository.getProjectById(projectId)
            projectName.value = _project.value?.repoName ?: ""

            _project.value?.let { project ->
                // Load open files
                fileRepository.getOpenFiles(projectId).collectLatest {
                    _openFiles.value = it
                }
            }

            loadFileTree()
            _isLoading.value = false
        }
    }

    fun loadFileTree(path: String = "") {
        viewModelScope.launch {
            _project.value?.let { project ->
                fileRepository.getFolderContent(
                    owner = project.owner,
                    repo = project.repoName,
                    path = path
                ).onSuccess { items ->
                    val nodes = items.map { item ->
                        FileNode(
                            name = item.name,
                            path = item.path,
                            isDirectory = item.type == "dir",
                            sha = item.sha
                        )
                    }.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))

                    if (path.isEmpty()) {
                        _fileTree.value = nodes
                    }
                }
            }
        }
    }

    fun openFile(fileNode: FileNode) {
        viewModelScope.launch {
            _project.value?.let { project ->
                _isLoading.value = true

                fileRepository.getFileContent(
                    owner = project.owner,
                    repo = project.repoName,
                    path = fileNode.path
                ).onSuccess { fileContent ->
                    val decodedContent = fileRepository.decodeBase64Content(
                        fileContent.content ?: ""
                    )

                    val fileId = fileRepository.openFile(
                        projectId = project.id,
                        filePath = fileNode.path,
                        fileName = fileNode.name,
                        content = decodedContent,
                        sha = fileContent.sha
                    )

                    val openFile = fileRepository.getOpenFiles(project.id).collectLatest { files ->
                        _openFiles.value = files
                        _selectedFile.value = files.find { it.id == fileId }
                        _fileContent.value = decodedContent
                    }
                }.onFailure { error ->
                    _uiState.value = IdeUiState.Error(error.message ?: "Failed to load file")
                }

                _isLoading.value = false
            }
        }
    }

    fun selectFile(file: OpenFileEntity) {
        _selectedFile.value = file
        _fileContent.value = file.content ?: ""
    }

    fun closeFile(file: OpenFileEntity) {
        viewModelScope.launch {
            fileRepository.closeFile(file.id)
            if (_selectedFile.value?.id == file.id) {
                _selectedFile.value = null
                _fileContent.value = ""
            }
        }
    }

    fun updateFileContent(content: String) {
        _fileContent.value = content
        _selectedFile.value?.let { file ->
            viewModelScope.launch {
                fileRepository.updateFileContent(file.id, content, true)
            }
        }
    }

    fun saveCurrentFile() {
        viewModelScope.launch {
            _selectedFile.value?.let { file ->
                _project.value?.let { project ->
                    fileRepository.saveFile(
                        owner = project.owner,
                        repo = project.repoName,
                        path = file.filePath,
                        content = _fileContent.value,
                        commitMessage = "Update ${file.fileName}",
                        branch = project.defaultBranch,
                        sha = file.sha
                    ).onSuccess {
                        fileRepository.updateFileContent(file.id, _fileContent.value, false)
                        _uiState.value = IdeUiState.FileSaved
                    }.onFailure { error ->
                        _uiState.value = IdeUiState.Error(error.message ?: "Failed to save file")
                    }
                }
            }
        }
    }

    fun toggleFolder(folderNode: FileNode) {
        // Toggle folder expansion and load children if needed
        if (!folderNode.isExpanded) {
            loadFileTree(folderNode.path)
        }

        _fileTree.value = _fileTree.value.map { node ->
            if (node.path == folderNode.path) {
                node.copy(isExpanded = !node.isExpanded)
            } else {
                node
            }
        }
    }

    fun refreshFileTree() {
        loadFileTree()
    }

    fun toggleBuildPanel() {
        _showBuildPanel.value = !_showBuildPanel.value
    }

    fun clearBuildLogs() {
        _buildLogs.value = emptyList()
    }

    fun triggerBuild(buildType: String = "debug") {
        viewModelScope.launch {
            _project.value?.let { project ->
                buildRepository.triggerBuild(
                    owner = project.owner,
                    repo = project.repoName,
                    branch = project.defaultBranch,
                    buildType = buildType
                ).onSuccess { runId ->
                    _buildLogs.value = _buildLogs.value + "Build triggered (Run ID: $runId)"
                    watchBuildStatus(runId)
                }.onFailure { error ->
                    _buildLogs.value = _buildLogs.value + "Failed to trigger build: ${error.message}"
                }
            }
        }
    }

    private fun watchBuildStatus(runId: Long) {
        viewModelScope.launch {
            _project.value?.let { project ->
                buildRepository.watchBuildStatus(
                    owner = project.owner,
                    repo = project.repoName,
                    runId = runId
                ).collect { status ->
                    _buildStatus.value = status
                    _buildLogs.value = _buildLogs.value + "Build status: ${status.status}"

                    if (status.status == "completed") {
                        _buildLogs.value = _buildLogs.value + "Build completed: ${status.conclusion}"
                    }
                }
            }
        }
    }
}

sealed class IdeUiState {
    data object Idle : IdeUiState()
    data object Loading : IdeUiState()
    data object FileSaved : IdeUiState()
    data class Error(val message: String) : IdeUiState()
}
