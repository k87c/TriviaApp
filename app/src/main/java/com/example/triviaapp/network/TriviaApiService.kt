package com.example.triviaapp.network

import com.example.triviaapp.model.QuestionApi
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * TriviaApiService for fetching questions from the API
 *
 * getApiQuestions() fetches questions from the API with the specified amount and type
 */

interface TriviaApiService{
    @GET("api.php")
    suspend fun getApiQuestions(
        @Query("amount") amount: Int = 10,
        @Query("type") type: String = "multiple",
        @Query("category") category: Int
    ): Response<QuestionApiResponse>
}

@Serializable
data class QuestionApiResponse(
    val response_code: Int,
    val results: List<QuestionApi>
)