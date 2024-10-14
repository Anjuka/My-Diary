@file:OptIn(ExperimentalMaterial3Api::class)

package com.zimbu.mydiary.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.zimbu.mydiary.ui.theme.BackgroundMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericAppBar(
    title: String,
    onItemClick: (() -> Unit)?,
    icon: @Composable() (() -> Unit)?,
    iconState: MutableState<Boolean>
) {

    TopAppBar(
        title = { Text(text = title, style = TextStyle(color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Thin)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = BackgroundMain),
        actions = {
            IconButton(
                onClick = {
                    onItemClick?.invoke()
                          },
                content = {
                    if (iconState.value){
                        icon?.invoke()
                    }
                }
            )
        }
    )
}