package com.example.triviaapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.triviaapp.TriviaApplication
import com.example.triviaapp.data.Game
import com.example.triviaapp.data.GameRepository
import com.example.triviaapp.data.QuestionRepository
import com.example.triviaapp.data.TriviaPreferencesRepository
import com.example.triviaapp.model.Question
import com.example.triviaapp.model.getQuestions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

sealed interface GameUiState {
    data object Loading : GameUiState
    data class Error(val message: String) : GameUiState
    data object Success : GameUiState
    data object Home : GameUiState
    data object Records : GameUiState
}

data class GameViewState(
    val uiState: GameUiState = GameUiState.Loading,
    val recordsByCategory: List<Game> = emptyList(),
    val allGames: List<Game> = emptyList(),
    val questions: List<Question> = emptyList(),
    val playerName: String = "",
    val expanded: Boolean = false,
    val category: Category = categories.first(),
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

data class Category(
    val id: Int,
    val name: String,
)

val categories = listOf(
    Category(10, "Books"),
    Category(11, "Film"),
    Category(12, "Music"),
    Category(13, "Musicals"),
    Category(14, "Television"),
    Category(15, "Video Games"),
    Category(16, "Board Games"),
)

class GameViewModel(
    private val questionRepository: QuestionRepository,
    private val triviaPreferencesRepository: TriviaPreferencesRepository,
    private val gameRepository: GameRepository,
) : ViewModel() {
    // Game UI State
    private val _gameViewState = MutableStateFlow(GameViewState())
    val gameViewState: StateFlow<GameViewState> = _gameViewState.asStateFlow()


    init {
        // Load the record from the preferences
        viewModelScope.launch {
            val record = triviaPreferencesRepository.recordFlow.first()
            _gameViewState.value = _gameViewState.value.copy(actualRecord = record)
        }
        // Set the initial state to Home
        _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Home)
    }

    fun loadQuestions(playerName: String, quantity: Int, category: Category) {
        viewModelScope.launch {
            _gameViewState.value = _gameViewState.value.copy(uiState = GameUiState.Loading)
            // val questions = getQuestions(quantity)
            val questions = questionRepository.getQuestions(quantity, category.id).map { it.toQuestion() }
            _gameViewState.value = _gameViewState.value.copy(
                uiState = GameUiState.Success,
                numberOfQuestions = quantity,
                questions = questions,
                playerName = playerName,
                category = category,
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
                val game = Game(
                    name = _gameViewState.value.playerName,
                    score = _gameViewState.value.correctAnswers,
                    category = _gameViewState.value.category.name,
                )
                gameRepository.insertGame(game)
                if (_gameViewState.value.currentPercentage > _gameViewState.value.actualRecord) {
                    _gameViewState.value = _gameViewState.value.copy(
                        actualRecord = _gameViewState.value.currentPercentage,
                        newRecord = true,
                    )
                    triviaPreferencesRepository.writeTriviaPreferences(_gameViewState.value.currentPercentage)
                }
            }
        }
    }

    fun onRestartGame() {
        loadQuestions(_gameViewState.value.playerName, _gameViewState.value.numberOfQuestions, _gameViewState.value.category)
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

    fun listOfCategories() = categories

    fun onChangePlayerName(playerName: String) {
        _gameViewState.value = _gameViewState.value.copy(playerName = playerName)
    }

    fun onChangeCategory(category: String) {
        _gameViewState.value = _gameViewState.value.copy(
            category = categories.first { it.name == category },
            expanded = false,
        )
    }

    fun expandDropDownMenu(expanded: Boolean) {
        _gameViewState.value = _gameViewState.value.copy(expanded = expanded)
    }

    fun onGoToRecords() {
        viewModelScope.launch {
            loadRecords()
        }
    }

    private suspend fun loadRecords() {
        val bestGames = categories.map { category ->
            gameRepository.getBestGame(category.name).firstOrNull() ?: Game(
                name = "No player",
                score = 0,
                category = category.name,
            )
        }
        _gameViewState.value = _gameViewState.value.copy(
            uiState = GameUiState.Loading,
            allGames = gameRepository.getAllGames().firstOrNull() ?: emptyList(),
            recordsByCategory = bestGames,
        )
        _gameViewState.value = _gameViewState.value.copy(
            uiState = GameUiState.Records,
        )
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
                val gameRepository = application.container.gameRepository
                val triviaPreferencesRepository = application.triviaPreferencesRepository
                GameViewModel(
                    questionRepository = questionRepository,
                    gameRepository = gameRepository,
                     triviaPreferencesRepository = triviaPreferencesRepository)
            }
        }
    }
}