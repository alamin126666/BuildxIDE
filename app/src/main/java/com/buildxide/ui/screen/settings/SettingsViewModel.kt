package com.buildxide.ui.screen.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buildxide.data.local.entity.EditorSettingsEntity
import com.buildxide.domain.model.EditorSettings
import com.buildxide.domain.model.EditorTheme
import com.buildxide.domain.usecase.auth.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _editorSettings = mutableStateOf(EditorSettings())
    val editorSettings: State<EditorSettings> = _editorSettings

    val showLogoutDialog = mutableStateOf(false)

    fun updateFontSize(size: Int) {
        _editorSettings.value = _editorSettings.value.copy(fontSize = size)
    }

    fun updateTabSize(size: Int) {
        _editorSettings.value = _editorSettings.value.copy(tabSize = size)
    }

    fun updateWordWrap(enabled: Boolean) {
        _editorSettings.value = _editorSettings.value.copy(wordWrap = enabled)
    }

    fun updateShowLineNumbers(enabled: Boolean) {
        _editorSettings.value = _editorSettings.value.copy(showLineNumbers = enabled)
    }

    fun updateTheme(theme: EditorTheme) {
        _editorSettings.value = _editorSettings.value.copy(theme = theme)
    }

    fun logout() {
        getCurrentUserUseCase.logout()
    }
}
