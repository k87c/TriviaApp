package com.example.triviaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Int,
    val category: String,
)