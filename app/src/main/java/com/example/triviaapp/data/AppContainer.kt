package com.example.triviaapp.data

import android.content.Context
import com.example.triviaapp.network.TriviaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * AppContainer for providing dependencies
 */

interface AppContainer {
    val questionRepository: QuestionRepository
    val gameRepository: GameRepository
}

class DefaultAppcontainer(context: Context): AppContainer {
    private val baseUrl = "https://opentdb.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: TriviaApiService by lazy { retrofit.create(TriviaApiService::class.java) }

    override val questionRepository: QuestionRepository by lazy { NetworkQuestionsRepository(retrofitService) }

    // Aquí empiezo con la parte de Room
    private val appDatabase = AppDatabase.getDatabase(context)
    private val gameDao = appDatabase.gameDao()
    override val gameRepository: GameRepository by lazy { GameRepositoryImpl(gameDao) }

}