package com.mendoxy.mnotes.ui.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mendoxy.mnotes.domain.model.SettingsModel
import com.mendoxy.mnotes.domain.repository.FirebaseAuthRepository
import com.mendoxy.mnotes.domain.repository.SettingsRepository
import com.mendoxy.mnotes.ui.utils.AppFontSize
import com.mendoxy.mnotes.ui.utils.AppTheme
import com.mendoxy.mnotes.ui.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    private val firebaseAuth: FirebaseAuth, // Renombrado para claridad (antes 'user')
    private val authRepository: FirebaseAuthRepository // Renombrado para claridad (antes 'auth')
) : ViewModel() {

    private val _settingsState = MutableStateFlow(
        SettingsState(
            userIsLogged = firebaseAuth.currentUser != null,
            isLoading = true // <--- Arrancamos cargando para confirmar con el listener
        )
    )
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    // Job específico para la suscripción a Firestore
    private var settingsJob: Job? = null

    init {
        // Al iniciar, nos suscribimos a los cambios de sesión.
        // Esto maneja AUTOMÁTICAMENTE el inicio de app, el login y el logout.
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            callbackFlow {
                val listener = FirebaseAuth.AuthStateListener { auth ->
                    trySend(auth.currentUser)
                }
                firebaseAuth.addAuthStateListener(listener)
                awaitClose { firebaseAuth.removeAuthStateListener(listener) }
            }.collect { user ->
                if (user != null) {
                    // 1. USUARIO LOGUEADO
                    Log.d("SettingsVM", "Usuario detectado: ${user.uid}")
                    _settingsState.update {
                        it.copy(
                            userIsLogged = true,
                            isLoading = false // <--- ¡Listo! Confirmado, quitamos carga
                        )
                    }
                    loadUserSettings(user.uid)
                } else {
                    // 2. USUARIO DESLOGUEADO
                    Log.d("SettingsVM", "No hay usuario activo")
                    stopListening()
                    _settingsState.update {
                        SettingsState(
                            userIsLogged = false,
                            isLoading = false // <--- ¡Listo! Confirmado que no hay nadie
                        )
                    }
                }
            }
        }
    }

    // Eliminé la función 'loadSettings()' pública.
    // Ya no la necesitas y causaba conflictos. 'observeAuthState' hace su trabajo.

    private fun loadUserSettings(userId: String) {
        // Limpieza preventiva
        stopListening()

        settingsJob = viewModelScope.launch {
            repository.getSettingsByUser(userId)
                // --- PROTECCIÓN CONTRA CRASHES ---
                // Si ocurre un error de permisos (común en logout rápido),
                // lo atrapamos aquí para que no cierre la app.
                .catch { e ->
                    Log.e("SettingsVM", "Error leyendo settings: ${e.message}")
                }
                .collect { remoteSettings ->
                    if (remoteSettings != null) {
                        _settingsState.update { state ->
                            state.copy(
                                appTheme = remoteSettings.theme,
                                fontSize = remoteSettings.fontSize,
                                order = remoteSettings.order
                            )
                        }
                    } else {
                        // Usuario nuevo: Crear configuración por defecto
                        saveSettingsToBackend(_settingsState.value, userId)
                    }
                }
        }
    }

    private fun saveSettingsToBackend(currentState: SettingsState, userId: String) {
        // Validación de seguridad: No guardar si el ID está vacío
        if (userId.isBlank()) return

        viewModelScope.launch {
            try {
                val modelToSave = SettingsModel(
                    userId = userId,
                    theme = currentState.appTheme,
                    fontSize = currentState.fontSize,
                    order = currentState.order
                )
                repository.updateSettingsForUser(userId = userId, settings = modelToSave)
            } catch (e: Exception) {
                Log.e("SettingsVM", "Error guardando settings: ${e.message}")
            }
        }
    }

    fun onEvent(event: SettingsEvent) {
        // Obtenemos el UID de forma segura
        val userId = firebaseAuth.currentUser?.uid ?: ""

        when (event) {
            is SettingsEvent.ChangeTheme -> {
                _settingsState.update { state -> state.copy(appTheme = event.theme) }
                // Guardado automático (Auto-save) es mejor UX que botón manual
                if (userId.isNotEmpty()) saveSettingsToBackend(_settingsState.value, userId)
            }
            is SettingsEvent.ChangeFontSize -> {
                _settingsState.update { state -> state.copy(fontSize = event.fontSize) }
                if (userId.isNotEmpty()) saveSettingsToBackend(_settingsState.value, userId)
            }
            is SettingsEvent.ChangeSortOrder -> {
                _settingsState.update { state -> state.copy(order = event.order) }
                if (userId.isNotEmpty()) saveSettingsToBackend(_settingsState.value, userId)
            }
            is SettingsEvent.SavePreferences -> {
                if (userId.isNotEmpty()) saveSettingsToBackend(_settingsState.value, userId)
            }
        }
    }

    private fun stopListening() {
        settingsJob?.cancel()
        settingsJob = null
    }

    fun logOut() {
        // 1. PRIMERO: Cortar la conexión con Firestore
        stopListening()

        // 2. Limpiar UI inmediatamente
        _settingsState.value = SettingsState(userIsLogged = false)

        // 3. AL FINAL: Cerrar sesión en Firebase
        // Como el listener ya murió en el paso 1, no habrá error de permisos.
        authRepository.logOut()
    }
}

// Data Class y Sealed Class se mantienen igual...
data class SettingsState(
    val appTheme: AppTheme = AppTheme.DARK,
    val fontSize: AppFontSize = AppFontSize.MIDDLE,
    val order: SortOrder = SortOrder.DESCENDING,
    val userIsLogged: Boolean = false,
    val isLoading: Boolean = false
)

sealed class SettingsEvent {
    data class ChangeTheme(val theme: AppTheme) : SettingsEvent()
    data class ChangeFontSize(val fontSize: AppFontSize) : SettingsEvent()
    data class ChangeSortOrder(val order: SortOrder) : SettingsEvent()
    data object SavePreferences : SettingsEvent()
}