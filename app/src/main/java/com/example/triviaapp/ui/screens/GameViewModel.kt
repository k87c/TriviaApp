package com.example.triviaapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.model.Question
import com.example.triviaapp.model.getQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface GameUiState {
    data object Loading : GameUiState
    data class Error(val message: String) : GameUiState
    data object Success : GameUiState
}

data class GameViewState(
    val uiState: GameUiState = GameUiState.Loading,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val numberOfQuestions: Int = 5,
    val questionReplied: Boolean = false,
    val currentQuestion: Question? = null,
    val correctPercentage: Int = 0,
    val actualRecord: Int = 0,
    val gameFinished: Boolean = false,
    val newRecord: Boolean = false,
)
class GameViewModel(quantity: Int, record: Int) : ViewModel() {
    // Game UI State
    private val _gameViewState = MutableStateFlow(GameViewState())
    val gameViewState: StateFlow<GameViewState> = _gameViewState.asStateFlow()

    init {
        loadQuestions(quantity, record)
    }

    fun loadQuestions(quantity: Int, record: Int) {
        _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Loading)
        val questions = getQuestions(quantity)
        _gameViewState.value = _gameViewState.value.copy(
            uiState = GameUiState.Success,
            numberOfQuestions = quantity,
            actualRecord = record,
            questions = questions,
            currentQuestionIndex = 0,
            correctAnswers = 0,
            questionReplied = false,
            correctPercentage = 0,
            gameFinished = false,
            newRecord = false,
            currentQuestion = questions.firstOrNull(),
        )
    }

    fun onAnswerSelected(answer: String) {
        viewModelScope.launch {
            val currentQuestion = _gameViewState.value.questions.getOrNull(_gameViewState.value.currentQuestionIndex)
            if (currentQuestion != null) {
                val correctAnswers = _gameViewState.value.correctAnswers + if (currentQuestion.validateAnswer(
                        answer
                    )
                ) 1 else 0
                _gameViewState.value = _gameViewState.value.copy(
                    correctAnswers = correctAnswers,
                    correctPercentage = correctAnswers * 100 / (_gameViewState.value.currentQuestionIndex + 1),
                    questionReplied = true,
                )
            }
        }
    }

    fun onNextQuestion() {
        viewModelScope.launch {
            val currentQuestionIndex = _gameViewState.value.currentQuestionIndex + 1
            if (currentQuestionIndex < _gameViewState.value.numberOfQuestions) {
                _gameViewState.value = _gameViewState.value.copy(
                    currentQuestionIndex = currentQuestionIndex,
                    currentQuestion = _gameViewState.value.questions.getOrNull(currentQuestionIndex),
                    questionReplied = false,
                )
            } else {
                _gameViewState.value = _gameViewState.value.copy(gameFinished = true)
                if (_gameViewState.value.correctPercentage > _gameViewState.value.actualRecord) {
                    _gameViewState.value = _gameViewState.value.copy(
                        actualRecord = _gameViewState.value.correctPercentage,
                        newRecord = true,
                    )
                }
            }
        }
    }

    fun onRestartGame() {
        loadQuestions(_gameViewState.value.numberOfQuestions, _gameViewState.value.actualRecord)
    }

    fun lessQuestions() {
        if (_gameViewState.value.numberOfQuestions > 5) _gameViewState.value = _gameViewState.value.copy(numberOfQuestions = _gameViewState.value.numberOfQuestions - 1)
    }

    fun moreQuestions() {
        if(_gameViewState.value.numberOfQuestions < 20) _gameViewState.value = _gameViewState.value.copy(numberOfQuestions = _gameViewState.value.numberOfQuestions + 1)
    }
}