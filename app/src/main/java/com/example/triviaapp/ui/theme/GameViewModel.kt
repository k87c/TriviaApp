package com.example.triviaapp.ui.theme

import androidx.lifecycle.ViewModel
import com.example.triviaapp.model.Question
import com.example.triviaapp.model.getQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface GameUiState {
    object Loading : GameUiState
    data class Error(val message: String) : GameUiState
    object Success : GameUiState
}

data class GameViewState(
    val uiState: GameUiState = GameUiState.Loading,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val numberOfQuestions: Int = 5,
    val questionResposed: Boolean = false,
    val currentQuestion: Question? = null,
)

class GameViewModel : ViewModel() {
    // Game UI State
    private val _gameViewState = MutableStateFlow(GameViewState())
    val gameViewState: StateFlow<GameViewState> = _gameViewState.asStateFlow()

    init {
        // Load questions
        loadQuestions()
    }

    private fun loadQuestions() {
        _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Loading)
        _gameViewState.value = _gameViewState.value.copy(
            uiState = GameUiState.Success,
            questions = getQuestions(_gameViewState.value.numberOfQuestions),
            currentQuestionIndex = 0,
        )
    }

    fun onAnswerSelected(answer: String) {
        val currentQuestion = _gameViewState.value.questions.getOrNull(_gameViewState.value.currentQuestionIndex)
        if (currentQuestion != null) {
            val question = currentQuestion
            val correctAnswers = _gameViewState.value.correctAnswers + if (question.validateAnswer(answer)) 1 else 0
            _gameViewState.value = _gameViewState.value.copy(
                correctAnswers = correctAnswers,
                questionResposed = true,
            )
        }
    }

    fun onNextQuestion() {
        val currentQuestionIndex = _gameViewState.value.currentQuestionIndex + 1
        if (currentQuestionIndex < _gameViewState.value.numberOfQuestions) {
            _gameViewState.value = _gameViewState.value.copy(
                questionResposed = false,
            )
        }
    }
}