package com.example.triviaapp.model

import com.example.triviaapp.data.questions

/**
 * Represents a question in the quiz
 */

data class Question (
    private val rawQuestion: String,
    private val rawOptions: List<String>,
    private val rawCorrectAnswer: String
) {
    val question: String
        get() = rawQuestion.decodeHtml()

    val options: List<String>
        get() = rawOptions.map { it.decodeHtml() }

    val correctAnswer: String
        get() = rawCorrectAnswer.decodeHtml()

    fun validateAnswer(answer: String): Boolean {
        return answer == correctAnswer
    }
}

private fun String.decodeHtml(): String {
    return android.text.Html.fromHtml(this, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
}

fun getQuestions(numberOfQuestions: Int) : List<Question> {
    return questions.shuffled().take(numberOfQuestions)
}
