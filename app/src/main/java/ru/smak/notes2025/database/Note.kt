package ru.smak.notes2025.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var title: String = "",
    var text : String = "",
    @ColumnInfo(name = "creation_time")
    val creationTime: LocalDateTime = LocalDateTime.now(),
){
    val isEmpty: Boolean
        get() = title.isBlank() && text.isBlank()
}