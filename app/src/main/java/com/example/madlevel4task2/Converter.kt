package com.example.madlevel4task2

import androidx.room.TypeConverter
import java.util.*

class Converter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun toChoice(value: Int) = enumValues<Choice>()[value]

    @TypeConverter
    fun fromChoice(value: Choice) = value.ordinal
}
