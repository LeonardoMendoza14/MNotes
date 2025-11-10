package com.mendoxy.mnotes.ui.presentation.mainScreen.home

import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.navigation.AppRoutes
import com.mendoxy.mnotes.ui.presentation.components.DefaultText
import com.mendoxy.mnotes.ui.presentation.components.DefaultTextField
import com.mendoxy.mnotes.ui.presentation.components.LoginTextField
import com.mendoxy.mnotes.ui.presentation.components.NoteCard
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.HomeNoteBottomSheet.NoteBottomSheetContent
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.homeViewModel.HomeViewModel
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import com.mendoxy.mnotes.ui.theme.dimenBig
import com.mendoxy.mnotes.ui.theme.dimenExtraLarge
import com.mendoxy.mnotes.ui.theme.dimenHuge
import com.mendoxy.mnotes.ui.theme.dimenLarge
import com.mendoxy.mnotes.ui.theme.dimenMiddle
import com.mendoxy.mnotes.ui.theme.dimenSmall
import com.mendoxy.mnotes.ui.utils.format
import com.mendoxy.mnotes.ui.utils.isToday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel = hiltViewModel()
) {

//    Que se muestre un icono de sincronizar a un lado de el icono de editar nota si la nota.isSync == false

    val notesCounter = vm.notesCounter.collectAsState().value
    val currentUser = vm.currentUser.value
    val notes = vm.notes.collectAsState().value
    val uiState = vm.uiState.value
    val isUserLoggedIn by vm.isUserLoggedIn.collectAsState()

    LaunchedEffect(isUserLoggedIn){
        if(!isUserLoggedIn){
            navController.navigate(AppRoutes.Login.route){
                popUpTo(0){inclusive = true}
                launchSingleTop = true
            }
        }
    }



    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }


    var selectedNote by remember { mutableStateOf<NoteModel?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HomeTopBar.HomeTopBar(notesCounter, onLogOutClick = {vm.logOut()})
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedNote = null
                    showBottomSheet = true
                },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "", tint = MaterialTheme.colorScheme.background)
            }
        }
    ){ paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimenLarge),
            contentAlignment = Alignment.TopCenter
        ){
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimenMiddle),
            ) {
                items(notes){ note ->
                    if(!note.isDeleted) {
                        NoteCard(
                            title = note.title,
                            date = if (note.date.isToday()) {
                                stringResource(R.string.note_isToday)
                            } else {
                                note.date.format()
                            },
                            isSync = note.isSync,
                            onSyncClick = {
                                vm.syncNote(note)
                            },
                            onDeleteClick = {
                                vm.deleteNote(currentUser!!.uid, note.idNote, note = note)
                            },
                            onEditClick = {
                                selectedNote = note
                                showBottomSheet = true
                            }
                        ) {
                            DefaultText(
                                text = note.content,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    NoteBottomSheetContent(
                        note = selectedNote,
                        onSave = { title, content ->
                            if (selectedNote == null) {
                                vm.addNote(userId = currentUser!!.uid, title = title, content = content)
                            } else {
                                vm.updateNote(note = selectedNote!!, title = title, content = content)
                            }
                            showBottomSheet = false
                        }
                    )
                }
            }
        }

    }
}



private object HomeTopBar{
    @Composable
    fun HomeTopBar(
        counter:Int,
        onLogOutClick: () -> Unit= {}){
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .padding(top = dimenExtraLarge)
                    .padding(horizontal = dimenLarge)
                    .systemBarsPadding(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    DefaultText(
                        text = stringResource(R.string.app_name),
                        color = MaterialTheme.colorScheme.primary,
                        isTitle = true
                    )

                    Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        modifier = Modifier
                            .clickable {
                                onLogOutClick()
                            }
                            .size(40.dp)
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary
                            )
                            .clip(CircleShape),
                        contentDescription = ""
                    )
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(dimenSmall),
                    modifier = Modifier
                        .padding(vertical = dimenBig)
                ) {
                    DefaultText(
                        text = counter.toString(),
                        color = MaterialTheme.colorScheme.secondary
                    )
                    DefaultText(
                        text = stringResource(R.string.home_notes),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

            }
        }
    }
}

private object HomeNoteBottomSheet{
    @Composable
    fun NoteBottomSheetContent(
        note: NoteModel?,
        onSave: (String, String) -> Unit
    ) {
        var title by remember { mutableStateOf(note?.title ?: "") }
        var content by remember { mutableStateOf(note?.content ?: "") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DefaultText(
                text = stringResource(if (note == null) R.string.home_createNote else R.string.home_editNote),
                color = MaterialTheme.colorScheme.primary,
                isTitle = true
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Contenido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 10,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                )
            )

            Button(
                onClick = { onSave(title, content) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(text = stringResource(if (note == null) R.string.home_save else R.string.home_update))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen(){
    MNotesTheme {
        HomeScreen(rememberNavController())
    }
}
