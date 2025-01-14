package com.example.triviaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    increseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit,
    quantity: Int = 5,
    onStartGame: (Int) -> Unit,
    record: Int = 0,
) {
    // Home screen content
    Column (
        // Column content alignment
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Home screen title
        Text("Trivia App", style = MaterialTheme.typography.titleLarge)
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        )
        {
            IconButton(
                onClick = decreaseQuantity
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Decrease questions"
                )
            }
            Text("Questions: $quantity")
            IconButton(
                onClick = increseQuantity
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Increase questions"
                )
            }
        }
        // Start game button
        Button(
            onClick = {
                onStartGame(quantity)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Start Game")
        }
        Row {
            Text("Actual record: $record %")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        increseQuantity = {},
        decreaseQuantity = {},
        onStartGame = {},
    )
}