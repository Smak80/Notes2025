package ru.smak.notes2025.models

import java.time.LocalDateTime

data class Note(
    var title: String = "",
    var text : String = "",
    val creation: LocalDateTime = LocalDateTime.now(),
)
