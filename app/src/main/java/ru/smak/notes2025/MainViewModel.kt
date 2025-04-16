package ru.smak.notes2025

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.smak.notes2025.models.Note

class MainViewModel : ViewModel() {

    var currentNote: Note? = null
        private set
    var currentView by mutableStateOf(ViewType.LIST_MODE)

    val notes = mutableStateListOf<Note>()

    fun addNote(){
        currentNote = Note()
        currentView = ViewType.EDIT_MODE
    }
}

enum class ViewType{
    LIST_MODE, EDIT_MODE
}