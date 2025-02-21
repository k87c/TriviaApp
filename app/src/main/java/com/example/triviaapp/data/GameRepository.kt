package com.example.triviaapp.data

import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getAllGames(): Flow<List<Game>>
    fun getBestGame(category: String): Flow<Game>
    suspend fun insertGame(game: Game)
}

class GameRepositoryImpl(private val gameDao: GameDao) : GameRepository {
    override fun getAllGames(): Flow<List<Game>> = gameDao.getAllGames()
    override fun getBestGame(category: String): Flow<Game> = gameDao.getBestGame(category)
    override suspend fun insertGame(game: Game) = gameDao.insertGame(game)
}