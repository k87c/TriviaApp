package com.example.triviaapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.triviaapp.TriviaApplication
import com.example.triviaapp.data.QuestionRepository
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
    data object Home : GameUiState
}

data class GameViewState(
    val uiState: GameUiState = GameUiState.Loading,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val numberOfQuestions: Int = 5,
    val questionReplied: Boolean = false,
    val currentQuestion: Question? = null,
    val currentPercentage: Int = 0,
    val actualRecord: Int = 0,
    val gameFinished: Boolean = false,
    val newRecord: Boolean = false,
)


class GameViewModel(private val questionRepository: QuestionRepository) : ViewModel() {
    // Game UI State
    private val _gameViewState = MutableStateFlow(GameViewState())
    val gameViewState: StateFlow<GameViewState> = _gameViewState.asStateFlow()


    init {
        _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Home)
    }

    fun loadQuestions(quantity: Int) {
        viewModelScope.launch {
            _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Loading)
            // val questions = getQuestions(quantity)
            val questions = questionRepository.getQuestions(quantity).map { it.toQuestion() }
            _gameViewState.value = _gameViewState.value.copy(
                uiState = GameUiState.Success,
                numberOfQuestions = quantity,
                questions = questions,
                currentQuestionIndex = 0,
                correctAnswers = 0,
                questionReplied = false,
                currentPercentage = 0,
                gameFinished = false,
                newRecord = false,
                currentQuestion = questions.firstOrNull(),
            )
        }
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
                    currentPercentage = correctAnswers * 100 / (_gameViewState.value.currentQuestionIndex + 1),
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
                if (_gameViewState.value.currentPercentage > _gameViewState.value.actualRecord) {
                    _gameViewState.value = _gameViewState.value.copy(
                        actualRecord = _gameViewState.value.currentPercentage,
                        newRecord = true,
                    )
                }
            }
        }
    }

    fun onRestartGame() {
        loadQuestions(_gameViewState.value.numberOfQuestions)
    }

    fun decreaseQuantity() {
        if (_gameViewState.value.numberOfQuestions > 5) _gameViewState.value = _gameViewState.value.copy(numberOfQuestions = _gameViewState.value.numberOfQuestions - 1)
    }

    fun increaseQuantity() {
        if(_gameViewState.value.numberOfQuestions < 20) _gameViewState.value = _gameViewState.value.copy(numberOfQuestions = _gameViewState.value.numberOfQuestions + 1)
    }

    fun onBackToHome() {
        _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Home)
    }


    // ViewModel Factory
    /*
    Explicación de la función viewModelFactory:
    - Esta función crea un ViewModelProvider.Factory que inicializa un GameViewModel.
    - La función initializer se encarga de inicializar el GameViewModel con el QuestionRepository.
    - La función viewModelFactory recibe un lambda que retorna un ViewModelProvider.Factory.
    - El lambda recibe un mapa de parámetros y retorna un ViewModel.
    - En este caso, el lambda recibe un mapa con un parámetro APPLICATION_KEY que es un TriviaApplication.
    - El ViewModel se inicializa con el QuestionRepository obtenido del TriviaApplication.
    Este companion object es necesario para poder crear un ViewModelProvider.Factory que inicialice un GameViewModel.
    La palabra reservada companion object permite definir un objeto que es parte de la clase GameViewModel.
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TriviaApplication)
                val questionRepository = application.container.questionRepository
                GameViewModel(questionRepository)
            }
        }
    }
}