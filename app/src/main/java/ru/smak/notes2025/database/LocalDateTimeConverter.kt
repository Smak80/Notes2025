package ru.smak.notes2025.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeConverter {
    @TypeConverter
    fun epochSecondToLocalDateTime(value: Long?): LocalDateTime? = value?.let{
        LocalDateTime.ofEpochSecond(
            it,
            0,
            ZoneOffset.of("+00")
        )
    }

    @TypeConverter
    fun localDateTimeToEpochDay(date: LocalDateTime?): Long? =
        date?.toEpochSecond(ZoneOffset.of("+00"))
}
