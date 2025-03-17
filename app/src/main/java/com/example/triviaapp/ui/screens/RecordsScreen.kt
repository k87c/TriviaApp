package com.example.triviaapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.record
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
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.onSecondary)
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(8.dp)
                        ,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Wrap each Text in a Box
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                        Text(
                            text = "Category",
                            color = MaterialTheme.colorScheme.onSecondary,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 4.dp, end = 16.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Player",
                            color = MaterialTheme.colorScheme.onSecondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Record",
                            color = MaterialTheme.colorScheme.onSecondary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(start = 16.dp, end = 4.dp)
                        )
                    }
                }
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
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSecondary)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Wrap each Text in a Box
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            Text(
                text = category,
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(end = 16.dp, start = 8.dp)
            )
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = record.toString(),
                color = MaterialTheme.colorScheme.onSecondary,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun GameInfo(
    game: Game,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(game.name)
        Text("${game.score} points in ${game.category}")
    }
}

@Preview(showBackground = true)
@Composable
fun RecordsScreenPreview() {
    RecordsScreen(
        recordsByCategory = listOf(
            Game(
                17, "Player 1", score = 10,
                category = "Books"
            ),
            Game(
                18, "Player 2", score = 20,
                category = "Video Games"
            ),
            Game(
                18, "Player 2", score = 5,
                category = "Films"
            ),
            Game(
                18, "Player 2", score = 20,
                category = "Music"
            ),
            Game(
                18, "Player 2", score = 20,
                category = "Television"
            ),
            Game(
                18, "mmmmmm", score = 25,
                category = "Board Games"
            ),
            Game(
                18, "Player 2", score = 45,
                category = "Musicals"
            ),
        ),
        allGames = listOf(
            Game(
                10, "Player 1", 10,
                category = "Musicals"
            ),
            Game(
                11, "Player 2", 20,
                category = "Games"
            ),
            Game(
                12, "Player 2", 20,
                category = "Games"
            ),
            Game(
                13, "Player 2", 20,
                category = "Games"
            ),
            Game(
                14, "Player 2", 20,
                category = "Games"
            ),
            Game(
                15, "Player 1", 20,
                category = "Games"
            ),
            Game(
                16, "Player 1", 20,
                category = "Games"
            ),
            Game(
                17, "Example Player Name", 20,
                category = "Games"
            ),
        ),
        onBack = {}
    )
}