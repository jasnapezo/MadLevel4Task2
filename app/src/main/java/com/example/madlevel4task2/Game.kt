package com.example.madlevel4task2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.madlevel4task2.Converter
import java.util.*

enum class Choice {
    ROCK, PAPER, SCISSOR
}

@Entity(tableName = "game_table")
@TypeConverters(Converter::class)
class Game (
    @ColumnInfo(name = "result")
    var result: String,

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "player_choice")
    var playerChoice: Choice,

    @ColumnInfo(name = "computer_choice")
    var computerChoice: Choice,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)