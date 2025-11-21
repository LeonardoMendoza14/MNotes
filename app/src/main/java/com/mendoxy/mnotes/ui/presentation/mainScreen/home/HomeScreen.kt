package com.mendoxy.mnotes.ui.presentation.mainScreen.home

import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.HomeBottomSheet.NoteBottomSheetContent
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.homeViewModel.HomeViewModel
import com.mendoxy.mnotes.ui.theme.MNotesTheme
import com.mendoxy.mnotes.ui.theme.dimenBig
import com.mendoxy.mnotes.ui.theme.dimenButton
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

    val notesCounter = vm.notesCounter.collectAsState().value
    val currentUser = vm.currentUser.value
    val notes = vm.notes.collectAsState().value
    val uiState = vm.uiState.value
    val isUserLoggedIn by vm.isUserLoggedIn.collectAsState()

    LaunchedEffect(isUserLoggedIn) {
        if (!isUserLoggedIn) {
            navController.navigate(AppRoutes.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    val configState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showConfigBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }


    var selectedNote by remember { mutableStateOf<NoteModel?>(null) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HomeTopBar.HomeTopBar(
                counter = notesCounter,
                onProfileClick = { showConfigBottomSheet = true })
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
                Icon(
                    Icons.Default.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = dimenLarge),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(dimenMiddle),
            ) {
                items(notes) { note ->
                    if (!note.isDeleted) {
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

                    HomeBottomSheet.NoteBottomSheetContent(
                        note = selectedNote,
                        onSave = { title, content ->
                            if (selectedNote == null) {
                                vm.addNote(
                                    userId = currentUser!!.uid,
                                    title = title,
                                    content = content
                                )
                            } else {
                                vm.updateNote(
                                    note = selectedNote!!,
                                    title = title,
                                    content = content
                                )
                            }
                            showBottomSheet = false
                        }
                    )
                }
            }

            if (showConfigBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showConfigBottomSheet = false },
                    sheetState = configState,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    HomeBottomSheet.ConfigBottomSheetContent(
                        onLogOutClick = {
                            showConfigBottomSheet = false
                            vm.logOut()
                        }
                    )
                }
            }
        }

    }
}


private object HomeTopBar {
    @Composable
    fun HomeTopBar(
        counter: Int,
        onProfileClick: () -> Unit = {}
    ) {
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
                                onProfileClick()
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

private object HomeBottomSheet {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ConfigBottomSheetContent(
        modifier: Modifier = Modifier,
        onLoginSaveClick: () -> Unit = {},
        onLogOutClick: () -> Unit = {}
    ) {
        Box(
            modifier = modifier
                .fillMaxHeight(0.8f)
                .padding(horizontal = dimenExtraLarge)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                DefaultText(
                    text = stringResource(R.string.homeConfig_Title),
                    color = MaterialTheme.colorScheme.primary,
                    leadingSpacing = 0.dp,
                    isTitle = true
                )

                Spacer(Modifier.height(dimenExtraLarge))

                Column {
                    DefaultText(
                        text = stringResource(R.string.homeConfig_themeTitle),
                        color = MaterialTheme.colorScheme.secondary,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_darktheme),
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = ""
                            )
                        },
                        leadingSpacing = dimenMiddle
                    )

                    Spacer(Modifier.height(dimenLarge))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ConfigSheetComponents.ThemeCard(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.homeConfig_themeLight),
                            icon = painterResource(R.drawable.ic_lightheme),
                            iconColor = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.width(dimenBig))

                        ConfigSheetComponents.ThemeCard(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.homeConfig_themeDark),
                            icon = painterResource(R.drawable.ic_darktheme),
                            iconColor = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    ConfigSheetComponents.DivisorLine()

                }

                Column {
                    DefaultText(
                        text = stringResource(R.string.homeConfig_fontSizeTitle),
                        color = MaterialTheme.colorScheme.secondary,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_fontsize),
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = ""
                            )
                        },
                        leadingSpacing = dimenMiddle
                    )

                    Spacer(Modifier.height(dimenLarge))

                    ConfigSheetComponents.DropdownSelector()
                }

                ConfigSheetComponents.DivisorLine()

                Column {
                    DefaultText(
                        text = stringResource(R.string.homeConfig_orderTitle),
                        color = MaterialTheme.colorScheme.secondary,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_swaporder),
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = ""
                            )
                        },
                        leadingSpacing = dimenMiddle
                    )

                    Spacer(Modifier.height(dimenLarge))

                    ConfigSheetComponents.DropdownSelector()
                }

                Spacer(Modifier.height(dimenBig))

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    onClick = {

                    }
                ) {
                    DefaultText(
                        text = stringResource(R.string.home_save),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(Modifier.height(dimenBig))

                DefaultText(
                    text = stringResource(R.string.homeConfig_logOut),
                    color = Color.Red,
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            tint = Color.Red,
                            contentDescription = ""
                        )
                    },
                    containerModifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            onLogOutClick()
                        }
                )
            }
        }
    }
}

private object ConfigSheetComponents {
    @Composable
    fun ThemeCard(
        modifier: Modifier = Modifier,
        text: String,
        icon: Painter = painterResource(R.drawable.ic_lightheme),
        iconColor: Color = MaterialTheme.colorScheme.onSurface,
        isSelected: Boolean = false,
        onClick: () -> Unit = {}
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .fillMaxHeight(0.2f)
                .clip(MaterialTheme.shapes.small)
                .border(
                    width = 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    shape = MaterialTheme.shapes.small
                )
                .background(color = MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = icon,
                    modifier = Modifier
                        .size(dimenHuge),
                    tint = if (isSelected) iconColor else MaterialTheme.colorScheme.onSurface,
                    contentDescription = ""
                )

                Spacer(Modifier.height(dimenMiddle))

                DefaultText(
                    text = text,
                    color = if (isSelected) iconColor else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DropdownSelector(
        options: List<String> = listOf("Opción A", "Opción B", "Opción C")
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(options[0]) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimenButton)
                    .clip(MaterialTheme.shapes.small)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = MaterialTheme.shapes.small
                    )
                    .background(color = MaterialTheme.colorScheme.background)
                    .menuAnchor(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DefaultText(
                    text = selectedOption,
                    color = MaterialTheme.colorScheme.primary,
                    containerModifier = Modifier
                        .padding(horizontal = dimenBig),
                    leadingSpacing = 0.dp
                )

                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }


            /*TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true, // MUY IMPORTANTE
                label = { Text("Selecciona una opción") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor() // NECESARIO para posicionar el menú
                    .fillMaxWidth()
            )*/

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            DefaultText(
                                text = option,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        },
                        onClick = {
                            selectedOption = option
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun DivisorLine(modifier: Modifier = Modifier) {
        Spacer(modifier = Modifier.height(dimenBig))
        Spacer(modifier = Modifier.height(3.dp).fillMaxWidth().background(color = MaterialTheme.colorScheme.outline))
        Spacer(modifier = Modifier.height(dimenBig))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    MNotesTheme {
        HomeScreen(rememberNavController())
    }
}
