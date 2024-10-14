package com.zimbu.mydiary.ui.noteEdit

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.zimbu.mydiary.Constants
import com.zimbu.mydiary.MyDiaryApp
import com.zimbu.mydiary.NotesViewModel
import com.zimbu.mydiary.R
import com.zimbu.mydiary.model.Note
import com.zimbu.mydiary.ui.GenericAppBar
import com.zimbu.mydiary.ui.noteList.NotesFab
import com.zimbu.mydiary.ui.theme.BackgroundField
import com.zimbu.mydiary.ui.theme.BackgroundMain
import com.zimbu.mydiary.ui.theme.MyDiaryTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NoteEditScreen(
    noteId: Int,
    navController: NavController,
    viewModel: NotesViewModel
) {
    val scope = rememberCoroutineScope()
    val note = remember { mutableStateOf(Constants.noteDetailPlaceHolder) }

    val currentNote = remember {
        mutableStateOf(note.value.note)
    }

    val currentTitle = remember {
        mutableStateOf(note.value.title)
    }

    val currentImage = remember {
        mutableStateOf(note.value.imageUri)
    }

    val saveButtonState = remember {
        mutableStateOf(false)
    }

    val getImageRequest =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument(),
            onResult = { uri ->
                if (uri != null) {
                    MyDiaryApp.getUriPermission(uri)
                }
                currentImage.value = uri.toString()
                if (currentImage.value != note.value.imageUri) {
                    saveButtonState.value = true
                }
            })

    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            note.value = viewModel.getNote(noteId) ?: Constants.noteDetailPlaceHolder
            currentNote.value = note.value.note
            currentTitle.value = note.value.title
            currentImage.value = note.value.imageUri
        }
    }

    MyDiaryTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    GenericAppBar(
                        title = stringResource(R.string.edit_note),
                        onItemClick = {
                            viewModel.updateNote(
                                Note(
                                    id = note.value.id,
                                    note = currentNote.value,
                                    title = currentTitle.value,
                                    imageUri = currentImage.value
                                )
                            )
                            navController.popBackStack()
                        },
                        icon = {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.save),
                                contentDescription = stringResource(R.string.edit_note),
                                tint = Color.White
                            )
                        },
                        iconState = saveButtonState
                    )
                },
                floatingActionButton = {
                    NotesFab(
                        contentDescription = stringResource(id = R.string.add_photo),
                        action = {
                            getImageRequest.launch(arrayOf("image/*"))
                        },
                        icon = R.drawable.flip_camera
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
                        .fillMaxSize()
                ) {
                    HorizontalDivider(color = BackgroundField, thickness = 1.dp)

                    if (currentImage.value != null && currentImage.value!!.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = (Uri.parse(currentImage.value)))
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxHeight(0.3f)
                                .fillMaxWidth()
                                .padding(6.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, top = 12.dp, end = 12.dp),
                        value = currentTitle.value,
                        colors = TextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            focusedContainerColor = BackgroundField,
                            unfocusedTextColor = Color.White,
                            unfocusedContainerColor = BackgroundField
                        ),
                        onValueChange = { value ->
                            currentTitle.value = value
                            if (currentTitle.value != note.value.title) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.note && currentTitle.value == note.value.title) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Title", modifier = Modifier.padding(bottom = 8.dp)) }
                    )
                    Spacer(modifier = Modifier.padding(12.dp))

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 12.dp, end = 12.dp),
                        value = currentNote.value,
                        colors = TextFieldDefaults.colors(
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            focusedContainerColor = BackgroundField,
                            unfocusedTextColor = Color.White,
                            unfocusedContainerColor = BackgroundField
                        ),
                        onValueChange = { value ->
                            currentNote.value = value
                            if (currentNote.value != note.value.note) {
                                saveButtonState.value = true
                            } else if (currentNote.value == note.value.note && currentTitle.value == note.value.title) {
                                saveButtonState.value = false
                            }
                        },
                        label = { Text(text = "Body", modifier = Modifier.padding(bottom = 8.dp)) }
                    )
                }
            }

        }
    }
}