package com.buildxide.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildxide.data.local.entity.ProjectEntity
import com.buildxide.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _projects = MutableStateFlow<List<ProjectEntity>>(emptyList())
    val projects: StateFlow<List<ProjectEntity>> = _projects

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            projectRepository.getLocalProjects().collectLatest {
                _projects.value = it
            }
        }
    }

    fun syncProjects() {
        viewModelScope.launch {
            _isLoading.value = true
            projectRepository.syncFromGitHub()
            _isLoading.value = false
        }
    }

    fun searchProjects(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                projectRepository.getLocalProjects().collectLatest {
                    _projects.value = it
                }
            } else {
                val results = projectRepository.searchProjects(query)
                _projects.value = results
            }
        }
    }
}
