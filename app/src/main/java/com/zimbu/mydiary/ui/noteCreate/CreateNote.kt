package com.zimbu.mydiary.ui.noteCreate

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.zimbu.mydiary.MyDiaryApp
import com.zimbu.mydiary.NotesViewModel
import com.zimbu.mydiary.R
import com.zimbu.mydiary.ui.GenericAppBar
import com.zimbu.mydiary.ui.noteList.NotesFab
import com.zimbu.mydiary.ui.theme.BackgroundField
import com.zimbu.mydiary.ui.theme.BackgroundMain
import com.zimbu.mydiary.ui.theme.MyDiaryTheme
import com.zimbu.mydiary.ui.theme.TextLight

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateNoteScreen(
    navController : NavController,
    viewModel: NotesViewModel
){
    val currentNote = remember {
        mutableStateOf("")
    }

    val currentTitle = remember {
        mutableStateOf("")
    }

    val currentImage = remember {
        mutableStateOf("")
    }

    val saveButtonState = remember {
        mutableStateOf(false)
    }

    val getImageRequest = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null){
                MyDiaryApp.getUriPermission(uri)
            }
            currentImage.value = uri.toString()
        })

    MyDiaryTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = { GenericAppBar(
                    title = stringResource(R.string.create_note),
                    icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.save),
                            contentDescription = stringResource(R.string.edit_note),
                            tint = Color.White
                        )
                    },
                    onItemClick = {
                        viewModel.createNote(
                            currentTitle.value,
                            currentNote.value,
                            currentImage.value
                        )
                        navController.popBackStack()
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
                        icon = R.drawable.camera
                    )
                },
                content = {

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
                        if (currentImage.value.isNotEmpty()) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(data = Uri.parse(currentImage.value))
                                        .build()
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxHeight(0.3f)
                                    .padding(6.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        TextField(
                            value = currentTitle.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, end = 12.dp, top = 10.dp),
                            colors = TextFieldDefaults.colors(
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                focusedContainerColor = BackgroundField,
                                unfocusedTextColor = TextLight,
                                unfocusedContainerColor = BackgroundField
                            ),
                            onValueChange = { value ->
                                currentTitle.value = value
                                saveButtonState.value =
                                    currentTitle.value != "" && currentNote.value != ""
                            },
                            label = { Text(text = "Title", modifier = Modifier.padding(bottom = 8.dp)) }
                        )
                        Spacer(modifier = Modifier.padding(12.dp))

                        TextField(value = currentNote.value,
                            modifier = Modifier
                                .fillMaxHeight(0.5f)
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp),
                            colors = TextFieldDefaults.colors(
                                cursorColor = Color.White,
                                focusedTextColor = Color.White,
                                focusedContainerColor = BackgroundField,
                                unfocusedTextColor = TextLight,
                                unfocusedContainerColor = BackgroundField
                            ),
                            onValueChange = { value ->
                                currentNote.value = value
                                saveButtonState.value =
                                    currentTitle.value != "" && currentNote.value != ""
                            },
                            label = { Text(text = "Body", modifier = Modifier.padding(bottom = 8.dp)) }
                        )

                    }
                }

            )
        }
    }
}