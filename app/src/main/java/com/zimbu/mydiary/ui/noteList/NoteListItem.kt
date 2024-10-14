package com.zimbu.mydiary.ui.noteList

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.zimbu.mydiary.Constants
import com.zimbu.mydiary.R
import com.zimbu.mydiary.model.Note

object NoteListItem{
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun NoteListItem(
        note: Note,
        openDialog: MutableState<Boolean>,
        deleteText: MutableState<String>,
        navController: NavController,
        noteBackground: Color,
        notesToDelete: MutableState<List<Note>>
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier
                .background(noteBackground)
                .height(120.dp)
                .fillMaxWidth()
                .combinedClickable(interactionSource = remember {
                    MutableInteractionSource()
                },
                    indication = rememberRipple(bounded = false),
                    onClick = {
                        if (note.id != 0) {
                            navController.navigate(
                                Constants.noteDetailNavigation(
                                    noteId = note.id ?: 0
                                )
                            )
                        }
                    },
                    onLongClick = {
                        if (note.id != 0) {
                            openDialog.value = true
                            deleteText.value = "Are you sure you want delete this note ?"
                            notesToDelete.value = mutableListOf(note)
                        }
                    }
                )
            ) {
                Row() {
                    if (!note.imageUri.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model =
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(Uri.parse(note.imageUri))
                                    .build()
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        if (note.title != "No Notes Found") {
                            Image(
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .fillMaxHeight()
                                    .padding(8.dp),
                                painter = painterResource(id = R.drawable.def_iamge),
                                contentDescription = ""
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxHeight(),
                        Arrangement.Center
                    ) {
                        Text(
                            text = note.title,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(fontSize = 20.sp)
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            text = note.note,
                            color = Color.White,
                            maxLines = 2,
                            fontWeight = FontWeight.Normal,
                            style = TextStyle(fontSize = 14.sp)
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            text = note.dateUpdated,
                            color = Color.Gray,
                            fontWeight = FontWeight.ExtraLight,
                            style = TextStyle(fontSize = 10.sp)
                        )
                    }
                }
            }
        }
    }

}
