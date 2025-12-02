package com.mendoxy.mnotes.ui.presentation.mainScreen.home.homeViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.domain.useCase.NotesUseCase
import com.mendoxy.mnotes.ui.utils.SortOrder
import com.mendoxy.mnotes.ui.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeState(
    val uiState: UiState = UiState.Idle,
    val notes: List<NoteModel> = emptyList(),
    val showBottomSheet: Boolean = false,
    val selectedNoteForEdit: NoteModel? = null,
    val showConfigSheet: Boolean = false,
    val name: String = "MN"
) {
    val notesCounter: Int get() = notes.size
}

sealed class HomeEvent() {
    data class AddNote(val title: String, val content: String) : HomeEvent()
    data class DeleteNote(val noteId: String) : HomeEvent()
    data class UpdateNote(val note: NoteModel, val title: String, val content: String) : HomeEvent()
    data class PinNote(val note: NoteModel) : HomeEvent()
    data class ShowNoteSheet(val note: NoteModel? = null) : HomeEvent()
    data object ShowConfigSheet : HomeEvent()
    data class ChangeOrder(val order: SortOrder) : HomeEvent()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: NotesUseCase,
    auth: FirebaseAuth
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState())
    val homeState: StateFlow<HomeState> = _homeState
    private val _orderList = MutableStateFlow(SortOrder.DESCENDING)
    private val _userId: String = auth.currentUser?.uid ?: ""
    private var _notesJob: Job? = null

    init {
        if (_userId.isNotEmpty()) {
            initialLoad()

            val user = auth.currentUser

            // 1. Lógica de prioridad: Nombre -> Email -> "MN" (MNotes)
            val nameSource = user?.displayName?.takeIf { it.isNotBlank() }
                ?: user?.email?.takeIf { it.isNotBlank() }
                ?: "MN"

            // 2. Procesamiento: 2 letras en mayúscula
            val initials = nameSource.take(2).uppercase()

            _homeState.update { state ->
                state.copy(name = initials)
            }
        }
    }

    fun onEvent(type: HomeEvent) {
        when (type) {
            is HomeEvent.AddNote -> {
                viewModelScope.launch {
                    useCase.createNote(_userId, type.title, type.content)
                }
            }
            is HomeEvent.DeleteNote -> {
                viewModelScope.launch {
                    useCase.deleteNote(_userId, type.noteId)
                }
            }
            is HomeEvent.UpdateNote -> {
                viewModelScope.launch {
                    val newNote = type.note.copy(
                        title = type.title,
                        content = type.content,
                        date = System.currentTimeMillis()
                    )
                    useCase.updateNote(_userId, newNote)
                }
            }
            is HomeEvent.PinNote -> {
                viewModelScope.launch {
                    val newNote = type.note.copy(pinned = !type.note.pinned)
                    useCase.updateNote(_userId, newNote)
                }
            }
            is HomeEvent.ShowNoteSheet -> {
                _homeState.update { state ->
                    state.copy(
                        selectedNoteForEdit = type.note,
                        showBottomSheet = !state.showBottomSheet
                    )
                }
            }
            is HomeEvent.ShowConfigSheet -> {
                _homeState.update { state ->
                    state.copy(showConfigSheet = !state.showConfigSheet)
                }
            }
            is HomeEvent.ChangeOrder -> {
                _orderList.value = type.order

            }
        }
    }

    private fun initialLoad() {
        _notesJob = viewModelScope.launch {
            // Combinamos los datos y el orden
            combine(
                useCase.getAllNotes(userId = _userId),
                _orderList
            ) { notes, order ->
                sortNotes(notes, order)
            }.collect { sortedNotes ->
                _homeState.update { state ->
                    state.copy(
                        notes = sortedNotes,
                        uiState = UiState.Success(true)
                    )
                }
            }
        }
    }

    // Función pura de ordenamiento en memoria
    private fun sortNotes(notes: List<NoteModel>, order: SortOrder): List<NoteModel> {
        return when (order) {
            SortOrder.ASCENDING -> {
                notes.sortedWith(
                    compareByDescending<NoteModel> { it.pinned } // 1. Pinned siempre arriba
                        .thenBy { it.date }                      // 2. Luego fecha Ascendente
                )
            }

            SortOrder.DESCENDING -> {
                notes.sortedWith(
                    compareByDescending<NoteModel> { it.pinned } // 1. Pinned siempre arriba
                        .thenByDescending { it.date }            // 2. Luego fecha Descendente
                )
            }
        }
    }

    fun closeListener(){
        _notesJob?.cancel()
        _notesJob = null

    }

}