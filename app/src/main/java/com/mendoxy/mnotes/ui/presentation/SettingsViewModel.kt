package com.mendoxy.mnotes.ui.presentation

import androidx.lifecycle.ViewModel
import com.mendoxy.mnotes.ui.utils.AppFontSize
import com.mendoxy.mnotes.ui.utils.AppTheme
import com.mendoxy.mnotes.ui.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) : ViewModel() {
    private val _settingsState = MutableStateFlow<SettingsState>(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    fun onEvent(event: SettingsEvent){
        when(event){
            is SettingsEvent.ChangeTheme -> {
                _settingsState.update { state ->
                    state.copy(appTheme = event.theme)
                }
            }
            is SettingsEvent.ChangeFontSize -> {
                _settingsState.update { state ->
                    state.copy(fontSize = event.fontSize)
                }
            }
            is SettingsEvent.ChangeSortOrder -> {
                _settingsState.update { state ->
                    state.copy(order = event.order)
                }
            }
            is SettingsEvent.SavePreferences -> {
                // TODO: Subir los cambios a firestore y aplicarlos al dispositivo
            }
            else -> {}
        }

    }

}

data class SettingsState(
    val appTheme: AppTheme = AppTheme.DARK,
    val fontSize: AppFontSize = AppFontSize.MIDDLE,
    val order: SortOrder = SortOrder.DESCENDING
)

sealed class SettingsEvent{
    data class ChangeTheme(val theme: AppTheme): SettingsEvent()
    data class ChangeFontSize(val fontSize: AppFontSize): SettingsEvent()
    data class ChangeSortOrder(val order: SortOrder): SettingsEvent()
    data object SavePreferences: SettingsEvent()
}