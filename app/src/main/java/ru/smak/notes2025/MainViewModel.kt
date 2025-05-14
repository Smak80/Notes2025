package ru.smak.notes2025

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.smak.notes2025.database.Note
import ru.smak.notes2025.database.NoteDao
import ru.smak.notes2025.database.NotesDatabase

class MainViewModel : ViewModel() {

    var currentNote: Note? = null
        private set
    var currentView by mutableStateOf(ViewType.LIST_MODE)
    var db: NoteDao? = null

    var notes: Flow<List<Note>>? = null//mutableStateListOf<Note>()

    fun loadNotes(context: Context){
        db = NotesDatabase.getDb(context)
        notes = db?.getAllNotes()
    }

    fun addNote(){
        currentNote = Note()
        currentView = ViewType.EDIT_MODE
    }

    fun editNote(note: Note){
        currentNote = note
        currentView = ViewType.EDIT_MODE
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            db?.deleteNote(note)
        }
    }

    fun toListMode(){
        currentView = ViewType.LIST_MODE
        currentNote?.let {
            if (!it.isEmpty){
                viewModelScope.launch {
                    if (it.id != 0L)
                        db?.updateNote(it)
                    else db?.addNote(it)
                }
            }
            else deleteNote(it)
        }
    }
}

enum class ViewType{
    LIST_MODE, EDIT_MODE
}