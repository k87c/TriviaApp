package com.example.triviaapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAllGames(): Flow<List<Game>>

    @Query("SELECT * FROM game WHERE category = :category ORDER BY score DESC LIMIT 1")
    fun getBestGame(category: String): Flow<Game>

    @Insert
    suspend fun insertGame(game: Game)
}