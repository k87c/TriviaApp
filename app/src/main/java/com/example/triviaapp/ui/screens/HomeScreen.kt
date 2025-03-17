package com.example.triviaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
    onStartGame: (String, Int, Category) -> Unit,
    playerName: String,
    onChangePlayerName: (String) -> Unit,
    category: Category,
    onChangeCategory: (String) -> Unit,
    categories: List<Category>,
    expanded: Boolean = false,
    expandDropDownMenu: (Boolean) -> Unit,
    record: Int = 0,
    onGoToRecords: () -> Unit,
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
        OutlinedTextField(
            label = { Text("Player Name") },
            value = playerName,
            onValueChange = onChangePlayerName,
            singleLine = true,
        )
        Box(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            TextButton(
                onClick =  {expandDropDownMenu(!expanded) }
            ) {
                Text(category.name)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand dropdown menu"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expandDropDownMenu(false)
                },
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            onChangeCategory(category.name)
                        },
                        text = {
                            Text("Categoría: " + category.name)
                        }
                    )
                }
            }
        }
        // Start game button
        Button(
            onClick = {
                onStartGame(playerName, quantity, category)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Start Game")
        }
        Row {
            Text("Actual record: $record %")
        }
        TextButton(
            onClick = { onGoToRecords() },
            modifier = Modifier.padding(8.dp),
        ) {
            Text("See All Records")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        increseQuantity = {},
        decreaseQuantity = {},
        onStartGame = { _, _, _ -> },
        playerName = "Player",
        onChangePlayerName = {},
        category = Category(1, "Category 1"),
        onChangeCategory = {},
        categories = listOf(
            Category(1, "Category 1"),
            Category(2, "Category 2"),
            Category(3, "Category 3"),
        ),
        expanded = false,
        expandDropDownMenu = {},
        onGoToRecords = {},
    )
}