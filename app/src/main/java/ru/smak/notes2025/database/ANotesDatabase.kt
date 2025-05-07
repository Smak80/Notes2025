package ru.smak.notes2025.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database([Note::class], version = 1, exportSchema = false)
@TypeConverters(LocalDateTimeConverter::class, )
abstract class ANotesDatabase : RoomDatabase(){

    abstract fun noteDao(): NoteDao
}

object NotesDatabase{
    private lateinit var db: NoteDao

    fun getDb(applicationContext: Context): NoteDao {
        if (!::db.isInitialized)
            db = Room.databaseBuilder(
                applicationContext,
                ANotesDatabase::class.java,
                "notes",
            ).build().noteDao()
        return db
    }
}