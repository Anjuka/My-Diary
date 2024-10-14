package com.zimbu.mydiary.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zimbu.mydiary.model.Note

@Database(
    entities = [
        Note::class], version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun NotesDao(): NotesDao
}