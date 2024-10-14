package com.zimbu.mydiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zimbu.mydiary.ui.noteList.NoteListScreen
import com.zimbu.mydiary.ui.noteCreate.CreateNoteScreen
import com.zimbu.mydiary.ui.noteDetail.NoteDetailPage
import com.zimbu.mydiary.ui.noteEdit.NoteEditScreen

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        viewModel = NoteViewModelFactory(MyDiaryApp.getDao()).create(NotesViewModel::class.java)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = Constants.NAVIGATION_NOTES_LIST){
                //Note List
                composable(Constants.NAVIGATION_NOTES_LIST){ NoteListScreen(
                    navController = navController,
                    viewModel = viewModel
                )}

                //Note details
                composable(Constants.NAVIGATION_NOTE_DETAIL,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTE_ID){
                     type = NavType.IntType   
                    })
                ){navBackStackEntry -> 
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_ID)?.let {
                        NoteDetailPage(noteId = it, navController = navController, viewModel = viewModel)
                    }
                }

                //Note edit
                composable(Constants.NAVIGATION_NOTE_EDIT,
                    arguments = listOf(navArgument(Constants.NAVIGATION_NOTE_ID){
                        type = NavType.IntType
                    })
                ){navBackStackEntry ->
                    navBackStackEntry.arguments?.getInt(Constants.NAVIGATION_NOTE_ID)?.let {
                        NoteEditScreen(noteId = it, navController = navController, viewModel = viewModel)
                    }
                }
                
                //Note Create page
                composable(Constants.NAVIGATION_NOTES_CREATE){
                    CreateNoteScreen(navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}