package ru.smak.notes2025.models

import java.time.LocalDateTime

data class Note(
    val title: String = "",
    val text : String = "",
    val creation: LocalDateTime = LocalDateTime.now(),
)
