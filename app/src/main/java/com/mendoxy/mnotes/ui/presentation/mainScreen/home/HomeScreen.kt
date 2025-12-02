package com.mendoxy.mnotes.ui.presentation.mainScreen.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mendoxy.mnotes.R
import com.mendoxy.mnotes.domain.model.NoteModel
import com.mendoxy.mnotes.navigation.AppRoutes
import com.mendoxy.mnotes.ui.presentation.SettingsEvent
import com.mendoxy.mnotes.ui.presentation.SettingsViewModel
import com.mendoxy.mnotes.ui.presentation.components.DefaultButton
import com.mendoxy.mnotes.ui.presentation.components.DefaultText
import com.mendoxy.mnotes.ui.presentation.components.NoteCard
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.homeViewModel.HomeEvent
import com.mendoxy.mnotes.ui.presentation.mainScreen.home.homeViewModel.HomeViewModel
import com.mendoxy.mnotes.ui.theme.dimenBig
import com.mendoxy.mnotes.ui.theme.dimenButton
import com.mendoxy.mnotes.ui.theme.dimenExtraHuge
import com.mendoxy.mnotes.ui.theme.dimenExtraLarge
import com.mendoxy.mnotes.ui.theme.dimenHuge
import com.mendoxy.mnotes.ui.theme.dimenLarge
import com.mendoxy.mnotes.ui.theme.dimenMiddle
import com.mendoxy.mnotes.ui.theme.dimenSmall
import com.mendoxy.mnotes.ui.utils.AppFontSize
import com.mendoxy.mnotes.ui.utils.AppTheme
import com.mendoxy.mnotes.ui.utils.SortOrder
import com.mendoxy.mnotes.ui.utils.format
import com.mendoxy.mnotes.ui.utils.isToday

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as ComponentActivity
    val svm: SettingsViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val settingsState by svm.settingsState.collectAsState()

    /**
     * TODO: Deuda tecnica, deberia implementar un background por si no hay notas o no las ha cargado
     */
    val homeState by vm.homeState.collectAsState()

    // 1. LÓGICA DE NAVEGACIÓN SEGURA
    // Solo navegamos al Login si YA TERMINAMOS de cargar Y el resultado fue "No hay usuario".
    LaunchedEffect(settingsState.userIsLogged, settingsState.isLoading) {
        if (!settingsState.isLoading && !settingsState.userIsLogged) {
            vm.closeListener()
            navController.navigate(AppRoutes.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    val configState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)



    if (!settingsState.isLoading) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                HomeTopBar.HomeTopBar(
                    counter = homeState.notesCounter,
                    name = homeState.name,
                    onProfileClick = { vm.onEvent(HomeEvent.ShowConfigSheet) })
            },

            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        vm.onEvent(HomeEvent.ShowNoteSheet())
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
                    items(homeState.notes) { note ->

                        NoteCard(
                            title = note.title,
                            date = if (note.date.isToday()) {
                                stringResource(R.string.note_isToday)
                            } else {
                                note.date.format()
                            },
                            isPinned = note.pinned,
                            onPinClick = {
                                vm.onEvent(HomeEvent.PinNote(note))
                            },
                            onDeleteClick = {
                                vm.onEvent(HomeEvent.DeleteNote(note.idNote))
                            },
                            onEditClick = {
                                vm.onEvent(HomeEvent.ShowNoteSheet(note = note))
                            }
                        ) {
                            DefaultText(
                                text = note.content,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }

                if (homeState.showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { vm.onEvent(HomeEvent.ShowNoteSheet()) },
                        sheetState = sheetState,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {

                        HomeBottomSheet.NoteBottomSheetContent(
                            note = homeState.selectedNoteForEdit,
                            onSave = { title, content ->
                                if (homeState.selectedNoteForEdit == null) {
                                    vm.onEvent(HomeEvent.AddNote(title, content))
                                } else {
                                    vm.onEvent(HomeEvent.UpdateNote(note = homeState.selectedNoteForEdit!!, title, content))
                                }
                                vm.onEvent(HomeEvent.ShowNoteSheet())
                            }
                        )
                    }
                }

                if (homeState.showConfigSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { vm.onEvent(HomeEvent.ShowConfigSheet)},
                        sheetState = configState,
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        HomeBottomSheet.ConfigBottomSheetContent(
                            currentTheme = settingsState.appTheme,
                            currentFontSize = settingsState.fontSize,
                            currentOrder = settingsState.order,
                            onSaveClick = {
                                svm.onEvent(SettingsEvent.SavePreferences)
                                vm.onEvent(HomeEvent.ShowConfigSheet)
                            },
                            onLogOutClick = {
                                vm.onEvent(HomeEvent.ShowConfigSheet)
                                svm.logOut()
                            },
                            onChangeTheme = { newTheme ->
                                svm.onEvent(SettingsEvent.ChangeTheme(newTheme))
                            },
                            onChangeFontSize = { newFontSize ->
                                svm.onEvent(SettingsEvent.ChangeFontSize(newFontSize))
                            },
                            onChangeOrder = { sortOrder ->
                                svm.onEvent(SettingsEvent.ChangeSortOrder(sortOrder))
                                vm.onEvent(HomeEvent.ChangeOrder(sortOrder))
                            }
                        )
                    }
                }
            }

        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}


private object HomeTopBar {
    @Composable
    fun HomeTopBar(
        counter: Int,
        name: String,
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

                    DefaultText(
                        text = name, // Ejemplo: Juan Nieve
                        color = MaterialTheme.colorScheme.primary,

                        // 1. Quitamos los espaciadores por defecto para que no empujen el texto
                        leadingSpacing = 0.dp,
                        trailingSpacing = 0.dp,

                        // 2. Centramos el contenido horizontalmente dentro del círculo
                        horizontalArrangement = Arrangement.Center,

                        // 3. Aplicamos la forma y tamaño al CONTENEDOR (Row), no al texto
                        containerModifier = Modifier
                            .size(40.dp) // El círculo contenedor mide 40dp
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface
                            )
                            .clip(CircleShape)
                            .clickable {
                                onProfileClick()
                            }
                    )

                    /*DefaultText(
                        text = "A",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.surface
                            )
                            .clip(shape = CircleShape)
                            .clickable {
                                onProfileClick()
                            }
                    )*/

                    /*Image(
                        painter = painterResource(R.drawable.ic_launcher_background),
                        modifier = Modifier

                            .size(40.dp)
                            .background(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary
                            )
                            .clip(CircleShape),
                        contentDescription = ""
                    )*/
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
        currentTheme: AppTheme = AppTheme.DARK,
        currentOrder: SortOrder = SortOrder.DESCENDING,
        currentFontSize: AppFontSize = AppFontSize.MIDDLE,
        onChangeTheme: (AppTheme) -> Unit = {},
        onChangeFontSize: (AppFontSize) -> Unit = {},
        onChangeOrder: (SortOrder) -> Unit = {},
        onSaveClick: () -> Unit = {},
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
                            isSelected = currentTheme == AppTheme.LIGHT,
                            text = stringResource(R.string.homeConfig_themeLight),
                            icon = painterResource(R.drawable.ic_lightheme),
                            iconColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                onChangeTheme(AppTheme.LIGHT)
                            }
                        )
                        Spacer(Modifier.width(dimenBig))

                        ConfigSheetComponents.ThemeCard(
                            modifier = Modifier.weight(1f),
                            isSelected = currentTheme == AppTheme.DARK,
                            text = stringResource(R.string.homeConfig_themeDark),
                            icon = painterResource(R.drawable.ic_darktheme),
                            iconColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                onChangeTheme(AppTheme.DARK)
                            }
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

                    ConfigSheetComponents.DropdownSelector(
                        selected = currentFontSize.ordinal,
                        options = listOf(
                            stringResource(R.string.homeConfig_smallFont),
                            stringResource(R.string.homeConfig_middleFont),
                            stringResource(R.string.homeConfig_bigFont)),
                        onChangeSelection = {index ->
                            onChangeFontSize(AppFontSize.entries[index])
                        }
                    )
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

                    ConfigSheetComponents.DropdownSelector(
                        selected = currentOrder.ordinal,
                        options = listOf(
                            stringResource(R.string.homeConfig_orderDesc),
                            stringResource(R.string.homeConfig_orderAsc)),
                        onChangeSelection = {index ->
                            onChangeOrder(SortOrder.entries[index])
                        }
                    )
                }

                Spacer(Modifier.height(dimenExtraHuge))

                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {
                        onSaveClick()
                    }
                ) {
                    DefaultText(
                        text = stringResource(R.string.home_save),
                        color = MaterialTheme.colorScheme.background
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
        iconColor: Color = MaterialTheme.colorScheme.primary,
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
                    color = if (isSelected) iconColor else MaterialTheme.colorScheme.onSurface,
                    shape = MaterialTheme.shapes.small
                )
                .background(color = MaterialTheme.colorScheme.background)
                .clickable {
                    onClick()
                },
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
        selected: Int,
        options: List<String> = listOf("Opción A", "Opción B", "Opción C"),
        onChangeSelection: (Int) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(options[selected]) }

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
                            onChangeSelection(options.indexOf(option))
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
        Spacer(modifier = Modifier
            .height(3.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.outline))
        Spacer(modifier = Modifier.height(dimenBig))
    }
}