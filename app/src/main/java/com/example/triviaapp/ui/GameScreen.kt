package com.example.triviaapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triviaapp.model.Question
import com.example.triviaapp.ui.theme.GameUiState
import com.example.triviaapp.ui.theme.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
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
            BottomAppBar (
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ){
                Row (
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Questions: ${gameViewState.currentQuestionIndex + 1}/${gameViewState.numberOfQuestions}")
                    Text("Correct Answers: ${gameViewState.correctAnswers}")
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
                        questionResponsed = gameViewState.questionResposed,
                        onAnswerSelected = { answer: String -> gameViewModel.onAnswerSelected(answer) },
                        onNextQuestion = { gameViewModel.onNextQuestion() }
                    )
                }
            }
        }
    }
}

@Composable
fun GameZone(question: Question?, questionResponsed : Boolean, onAnswerSelected: (String) -> Unit, onNextQuestion: () -> Unit) {
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
                        questionResponsed && option == question.correctAnswer
                    ) Color.Green else Color.Red).copy(alpha = 0.5f),
                ),
                enabled = questionResponsed.not()
            ) {
                Text(option)
            }
        }
        // Next button
        Button(
            onClick = { onNextQuestion() },
            modifier = Modifier.padding(8.dp)
                .fillMaxWidth(),
            enabled = questionResponsed,
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameScreen()
}
