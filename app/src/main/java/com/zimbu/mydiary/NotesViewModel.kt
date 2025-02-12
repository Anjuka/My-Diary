package com.zimbu.mydiary

import androidx.lifecycle.*
import com.zimbu.mydiary.model.Note
import com.zimbu.mydiary.persistence.NotesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(
    private val db : NotesDao
) : ViewModel() {
    val notes : LiveData<List<Note>> = db.getNotes()

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            db.deleteNote(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            db.updateNote(note)
        }
    }

    fun createNote(title: String, note: String, image: String? = null){
        val note = Note(title = title, note = note, imageUri = image)
        viewModelScope.launch(Dispatchers.IO){
            db.insertNote(note)
        }
    }

    suspend fun getNote(id : Int) : Note?{
        return db.getNoteById(id)
    }
}

class NoteViewModelFactory(
    private val db: NotesDao,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return  NotesViewModel(
            db = db,
        ) as T
    }
}