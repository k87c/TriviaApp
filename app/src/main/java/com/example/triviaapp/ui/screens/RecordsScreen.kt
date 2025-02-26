package com.example.triviaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.triviaapp.data.Game

@Composable
fun RecordsScreen(
    recordsByCategory: List<Game>,
    allGames: List<Game>,
    onBack: () -> Unit,
) {
    Column (
        // Column content alignment
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Records screen title
        Text("Records", style = MaterialTheme.typography.titleLarge)
        // Records by category
        if (recordsByCategory.isEmpty()) {
            Text("No records found")
        } else {
            Text("Records by category", style = MaterialTheme.typography.titleMedium)
            Column {
                recordsByCategory.forEach { game ->
                    CategoryRecord(game.category, game.name, game.score)
                }
            }

        }
        Spacer(modifier = Modifier.size(16.dp))
        if (allGames.isEmpty()) {
            Text("No records found")
        } else {
            Text("All games info", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                items(allGames) { game ->
                    GameInfo(game)
                }
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        // Back button
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun CategoryRecord(
    category: String,
    name: String,
    record: Int,
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.secondary).padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(category, color = MaterialTheme.colorScheme.onSecondary)
        Text("Player: $name", color = MaterialTheme.colorScheme.onSecondary)
        Text("Record: $record points", color = MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun GameInfo(
    game: Game,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(8.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(game.name)
        Text("${game.score} points in ${game.category}")
    }
}