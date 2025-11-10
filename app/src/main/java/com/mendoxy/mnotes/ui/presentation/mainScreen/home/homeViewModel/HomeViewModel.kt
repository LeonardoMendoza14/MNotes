package com.mendoxy.mnotes.ui.presentation.mainScreen.home.homeViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.useCase.NotesUseCase
import com.mendoxy.mnotes.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: NotesUseCase,
    auth: FirebaseAuth
): ViewModel() {
    private val _currentUser = mutableStateOf(auth.currentUser)
    val currentUser = _currentUser

    private val _isUserLoggedIn = MutableStateFlow(useCase.isUserLoggedIn())
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn


    private val _uiState = mutableStateOf<UiState>(UiState.Idle)
    val uiState = _uiState

    private val _notes = MutableStateFlow<List<NoteModel>>(emptyList())
    val notes: StateFlow<List<NoteModel>> = _notes

    val notesCounter: StateFlow<Int> = _notes
        .map { it.size }                     // Calcula el tamaño de la lista
        .stateIn(                            // Lo convierte en un StateFlow
            viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )

    init{
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            _currentUser.value?.let { user ->
                useCase.getAllNotes(user.uid, false).collect {
                    _notes.value = it
                    _uiState.value = UiState.Success(true)
                }
            }
        }

        viewModelScope.launch {
            useCase.syncPendingNotes(_currentUser.value!!.uid)
        }
    }

    fun addNote(userId: String, title: String, content: String){
        viewModelScope.launch {
            useCase.createNote(userId, title, content)
        }
    }

    fun deleteNote(userId: String, noteId: String, note: NoteModel){
        viewModelScope.launch{
            useCase.deleNote(userId, noteId, note = note)
        }
    }

    fun updateNote(note: NoteModel, title: String, content: String){
        viewModelScope.launch {
            useCase.updateNote(
                note = note.copy(
                    title = title,
                    content = content,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun syncNote(note: NoteModel){
        viewModelScope.launch {
            useCase.syncNote(note)
        }
    }

    fun logOut(){
        useCase.logOut()
        _isUserLoggedIn.value = false
    }

}