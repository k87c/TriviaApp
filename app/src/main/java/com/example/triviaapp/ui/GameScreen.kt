package com.example.triviaapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.triviaapp.model.Question
import com.example.triviaapp.ui.theme.GameUiState
import com.example.triviaapp.ui.theme.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel = viewModel()) {
    val gameViewState by gameViewModel.gameViewState.collectAsState()

    Scaffold (
        topBar = {
            // Game top bar
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
    Column {
        // Game question
        Text(question?.question ?: "No question found")
        // Game options
        question?.options?.forEach { option ->
            Button(
                onClick = { onAnswerSelected(option) },
                modifier = Modifier.padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary),

                enabled = questionResponsed.not()
            ) {
                Text(option)
            }
        }
        // Next button
        Button(
            onClick = { onNextQuestion() },
            modifier = Modifier.padding(8.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary),
            enabled = questionResponsed
        ) {
            Text("Next")
        }
    }

}
