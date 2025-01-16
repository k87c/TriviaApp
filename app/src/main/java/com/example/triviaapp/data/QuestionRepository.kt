package com.example.triviaapp.data

import com.example.triviaapp.model.QuestionApi
import com.example.triviaapp.network.TriviaApiService

/**
 * Repository for fetching questions from the network
 *
 */

interface QuestionRepository {
    suspend fun getQuestions(quantity: Int): List<QuestionApi>
}

class NetworkQuestionsRepository(
    private val triviaApiService: TriviaApiService
) : QuestionRepository {
    override suspend fun getQuestions(quantity: Int): List<QuestionApi> {
        val response = triviaApiService.getApiQuestions(
            amount = quantity
        )
        return if (response.isSuccessful) {
            response.body()?.results ?: emptyList()
        } else {
            emptyList()
        }
    }
}