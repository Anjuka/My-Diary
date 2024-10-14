package com.zimbu.mydiary

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.room.Room
import com.zimbu.mydiary.persistence.NoteDatabase
import com.zimbu.mydiary.persistence.NotesDao

class MyDiaryApp : Application() {
    private var db : NoteDatabase? = null

    init {
        instance = this
    }

    private fun getDb() : NoteDatabase {
        return if (db != null){
            db!!
        } else {
            db = Room.databaseBuilder(
                instance!!.applicationContext,
                NoteDatabase::class.java, Constants.DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()
            db!!
        }
    }

    companion object {
        private var instance: MyDiaryApp? = null

        fun getDao() : NotesDao {
            return instance!!.getDb().NotesDao()
        }

        fun getUriPermission(uri: Uri){
            instance!!.applicationContext.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }
}