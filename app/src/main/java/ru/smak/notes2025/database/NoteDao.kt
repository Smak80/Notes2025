package ru.smak.notes2025.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(entity = Note::class)
    suspend fun addNote(note: Note)

    @Update(Note::class)
    suspend fun updateNote(note: Note)

    @Delete(Note::class)
    suspend fun deleteNote(note: Note)

    @Query("select * from note where id=:id")
    suspend fun getNote(id: Long): Note?

    @Query("select * from note ORDER BY creation_time DESC")
    fun getAllNotes(): Flow<List<Note>>

}