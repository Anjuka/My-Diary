package com.zimbu.mydiary.ui.noteList

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zimbu.mydiary.model.Note
import com.zimbu.mydiary.ui.theme.DialogBack
import com.zimbu.mydiary.ui.theme.RedButton

object DeleteDialog {
    @Composable
    fun DeleteDialog(
        openDialog: MutableState<Boolean>,
        text: MutableState<String>,
        action: () -> Unit,
        notesToDelete: MutableState<List<Note>>
    ) {
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest =
                { openDialog.value = false },
                containerColor = DialogBack,
                title = {
                    Text(text = "Attention!",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    )
                },
                text = {
                    Text(text = text.value, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, color = Color.White))
                },
                confirmButton = {
                    Button(
                        colors = ButtonColors(containerColor = RedButton, disabledContainerColor = Color.White, contentColor = Color.White, disabledContentColor = Color.Black),
                        onClick = {
                            action.invoke()
                            openDialog.value = false
                            notesToDelete.value = mutableListOf()
                        }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(

                        onClick = {
                            openDialog.value = false
                            notesToDelete.value = mutableListOf()
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}