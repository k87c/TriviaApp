package com.example.triviaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triviaapp.model.Question
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameViewModel: GameViewModel = viewModel(factory = GameViewModel.Factory),
) {
    // Game view model
    val gameViewState by gameViewModel.gameViewState.collectAsState()
    Scaffold (
        topBar = {
            // Game top bar
            TopAppBar (
                title = { Text("Trivia App") },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            // Game bottom bar
            if (gameViewState.uiState == GameUiState.Success) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Question: ${gameViewState.currentQuestionIndex + 1}/${gameViewState.numberOfQuestions}")
                        Text(
                            "Correct Answers: ${gameViewState.currentPercentage} %",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) {
        // Game content
        Column (
            modifier = Modifier.padding(it).fillMaxSize(),
            horizontalAlignment =  Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Game UI
            when (gameViewState.uiState) {
                GameUiState.Home -> {
                    // Home state
                    HomeScreen(
                        increseQuantity = { gameViewModel.increaseQuantity() },
                        decreaseQuantity = { gameViewModel.decreaseQuantity() },
                        quantity = gameViewState.numberOfQuestions,
                        playerName = gameViewState.playerName,
                        onChangePlayerName = { playerName -> gameViewModel.onChangePlayerName(playerName) },
                        category = gameViewState.category,
                        onChangeCategory = { category -> gameViewModel.onChangeCategory(category) },
                        categories = gameViewModel.listOfCategories(),
                        expanded = gameViewState.expanded,
                        expandDropDownMenu = { expanded -> gameViewModel.expandDropDownMenu(expanded) },
                        onStartGame = { playerName: String, quantity: Int, category: Category ->
                            gameViewModel.loadQuestions(playerName,quantity, category)
                                      },
                        record = gameViewState.actualRecord,
                        onGoToRecords = { gameViewModel.onGoToRecords() }
                    )
                }
                GameUiState.Records -> {
                    // Records state
                    RecordsScreen(
                        recordsByCategory = gameViewState.recordsByCategory,
                        allGames = gameViewState.allGames,
                        onBack = { gameViewModel.onBackToHome() },
                    )
                }
                GameUiState.Loading -> {
                    // Loading state
                    Text("Loading...")
                }
                is GameUiState.Error -> {
                    // Error state
                    Text("Error: ${(gameViewState.uiState as GameUiState.Error).message}")
                }
                GameUiState.Success -> {
                    // Game state
                    GameZone(
                        question = gameViewState.currentQuestion,
                        questionReplied = gameViewState.questionReplied,
                        gameFinished = gameViewState.gameFinished,
                        newRecord = gameViewState.newRecord,
                        currentPercentage = gameViewState.currentPercentage,
                        onAnswerSelected = { answer: String -> gameViewModel.onAnswerSelected(answer) },
                        onNextQuestion = { gameViewModel.onNextQuestion()},
                        onRestartGame = { gameViewModel.onRestartGame() },
                        onBackToHome = { gameViewModel.onBackToHome() },
                        onGoToRecords = { gameViewModel.onGoToRecords() },
                    )
                }
            }
        }
    }
}

@Composable
fun GameZone(
    question: Question?,
    questionReplied : Boolean,
    gameFinished: Boolean,
    newRecord: Boolean,
    currentPercentage: Int = 0,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onRestartGame: () -> Unit,
    onBackToHome: () -> Unit,
    onGoToRecords: () -> Unit,
) {
    // Game zone
    Column (
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Game question
        Text(question?.question ?: "No question found",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary)
        // Game options
        question?.options?.forEach { option ->
            Button(
                onClick = { onAnswerSelected(option) },
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth(),
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = (if (
                        questionReplied && option == question.correctAnswer
                    ) Color.Green else Color.Red).copy(alpha = 0.5f),
                ),
                enabled = questionReplied.not()
            ) {
                Text(option)
            }
        }
        if (gameFinished) {
            // Final score dialog
            FinalScoreDialog(
                score = currentPercentage,
                newRecord = newRecord,
                onRestartGame = onRestartGame,
                onBackToHome = { onBackToHome() },
                onGoToRecords = { onGoToRecords() },
            )
        } else {
            // Next button
            Button(
                onClick = { onNextQuestion() },
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth(),
                enabled = questionReplied,
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                )
            ) {
                Text("Next")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinalScoreDialog(
    score: Int,
    newRecord: Boolean,
    onRestartGame: () -> Unit,
    onBackToHome: () -> Unit,
    onGoToRecords: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = { onBackToHome() },
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface),
        ) {

        Surface (
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Game finished!", style = MaterialTheme.typography.titleLarge)
                Text("Your score: $score %", style = MaterialTheme.typography.titleMedium)
                if (newRecord) {
                    Text(
                        "New record!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                TextButton(
                    onClick = { onGoToRecords() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("See Records")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = { onRestartGame() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Try Again")
                    }
                    TextButton(
                        onClick = { onBackToHome() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Go to Home")
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinalScoreDialogPreview() {
    FinalScoreDialog(
        score = 100,
        newRecord = true,
        onRestartGame = {},
        onBackToHome = {},
        onGoToRecords = {},
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameScreen()
}
