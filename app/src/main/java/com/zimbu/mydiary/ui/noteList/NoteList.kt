package com.zimbu.mydiary.ui.noteList

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.zimbu.mydiary.Constants
import com.zimbu.mydiary.Constants.orPlaceHolderList
import com.zimbu.mydiary.NotesViewModel
import com.zimbu.mydiary.R
import com.zimbu.mydiary.model.Note
import com.zimbu.mydiary.model.getDay
import com.zimbu.mydiary.ui.GenericAppBar
import com.zimbu.mydiary.ui.noteList.DeleteDialog.DeleteDialog
import com.zimbu.mydiary.ui.noteList.NoteListItem.NoteListItem
import com.zimbu.mydiary.ui.theme.BackgroundField
import com.zimbu.mydiary.ui.theme.BackgroundMain
import com.zimbu.mydiary.ui.theme.MyDiaryTheme
import com.zimbu.mydiary.ui.theme.OrangeStart
import com.zimbu.mydiary.ui.theme.TextLight

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteListScreen(navController: NavController, viewModel: NotesViewModel) {
    val deleteText = remember { mutableStateOf("") }
    val noteQuery = remember { mutableStateOf("") }
    val notesToDelete = remember { mutableStateOf(listOf<Note>()) }
    val openDialog = remember { mutableStateOf(false) }

    val notes = viewModel.notes.observeAsState()
    val context = LocalContext.current

    MyDiaryTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = stringResource(R.string.app_name),
                        onItemClick = {
                            if (notes.value?.isNotEmpty() == true) {
                                openDialog.value = true
                                deleteText.value = "Are you sure you want to delete all notes"
                                notesToDelete.value = notes.value ?: emptyList()
                            } else {
                                Toast.makeText(context, "No notes found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = stringResource(R.string.delete_note),
                                tint = Color.White
                            )
                        },
                        iconState = remember {
                            mutableStateOf(true)
                        }
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(id = R.string.create_note),
                        action = {
                            navController.navigate(Constants.NAVIGATION_NOTES_CREATE)
                        },
                        icon = R.drawable.add
                    )
                }
            ) {
                Box(
                    modifier = Modifier
                        .background(BackgroundMain)
                        .fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .padding(it)
                ) {
                    HorizontalDivider(
                        color = BackgroundField,
                        thickness = 1.dp)

                    SearchBar(query = noteQuery)
                    NoteList(
                        notes = notes.value.orPlaceHolderList(),
                        openDialog = openDialog,
                        query = noteQuery,
                        deleteText = deleteText,
                        navController = navController,
                        notesToDelete = notesToDelete
                    )
                }

                DeleteDialog(
                    openDialog = openDialog,
                    text = deleteText,
                    notesToDelete = notesToDelete,
                    action = {
                        notesToDelete.value.forEach { it ->
                            viewModel.deleteNote(it)
                        }
                    })
            }
        }

    }
}


@Composable
fun SearchBar(query: MutableState<String>) {
    Column(
        modifier = Modifier
            .padding(top = 12.dp, start = 12.dp, end = 12.dp, bottom = 0.dp)
    ) {
        TextField(
            value = query.value,
            placeholder = {
                Text(
                    text = "Search...",
                    maxLines = 1,
                    style = TextStyle(fontSize = 16.sp)
                )
            },
            maxLines = 1,
            onValueChange = { query.value = it },
            modifier = Modifier
                .background(BackgroundMain)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                focusedContainerColor = BackgroundField,
                unfocusedTextColor = TextLight,
                unfocusedContainerColor = BackgroundField
            ),
            trailingIcon = {
                AnimatedVisibility(
                    visible = query.value.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = { query.value = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.clear_search)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun NoteList(
    notes: List<Note>,
    openDialog: MutableState<Boolean>,
    query: MutableState<String>,
    deleteText: MutableState<String>,
    navController: NavController,
    notesToDelete: MutableState<List<Note>>
) {
    var previousHeader = ""
    LazyColumn(
        contentPadding = PaddingValues(12.dp),
        modifier = Modifier.background(Color.Transparent)
    ) {
        val queriedNotes = if (query.value.isEmpty()) {
            notes
        } else {
            notes.filter { it.note.contains(query.value) || it.title.contains(query.value) }
        }

        itemsIndexed(queriedNotes) { index, note ->
            if (note.getDay() != previousHeader) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = note.getDay(), color = TextLight, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold))
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )

                previousHeader = note.getDay()
            }

            NoteListItem(
                note,
                openDialog,
                deleteText,
                navController,
                BackgroundField,
                notesToDelete
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
        }
    }
}

@Composable
fun NotesFab(contentDescription: String, icon: Int, action: () -> Unit) {
    return FloatingActionButton(
        onClick = { action.invoke() },
        containerColor = OrangeStart
    ) {
        Icon(
            ImageVector.vectorResource(id = icon),
            contentDescription = contentDescription,
            tint = Color.White
        )
    }
}