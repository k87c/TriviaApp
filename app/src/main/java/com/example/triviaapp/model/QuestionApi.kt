package com.example.triviaapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a question in the quiz from the API
 *
 * toQuestion() converts the API model to the domain model
 */

@Serializable
data class QuestionApi(
    @SerialName("question")
    val question: String,
    @SerialName("correct_answer")
    val correct_answer: String,
    @SerialName("incorrect_answers")
    val incorrect_answers: List<String>
) {

    fun toQuestion(): Question {
        val options = incorrect_answers.toMutableList()
        options.add(correct_answer)
        return Question(question, options.shuffled(), correct_answer)
    }
}

