package com.example.triviaapp.model

import com.example.triviaapp.data.questions

data class Question (
    val question: String,
    val options: List<String>,
    val correctAnswer: String
) {
    fun validateAnswer(answer: String): Boolean {
        return answer == correctAnswer
    }
}

fun getQuestions(numberOfQuestions: Int) : List<Question> {
    return questions.shuffled().take(numberOfQuestions)
}