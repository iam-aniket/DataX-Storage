package com.example.dataxstore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@Composable
fun CoinDetailsScreen(viewModel: MainViewModel) {
    // Observe the coin details from DataStore
    val coinDetails by viewModel.coinDetails.collectAsState(initial = emptyMap())

    // Local state for UI input
    var newCoinKey by remember { mutableStateOf("") } // Input for coin name
    var newCoinValue by remember { mutableStateOf("") } // Input for coin value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text("Coin Details", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Display existing coins
        Text("Existing Coins", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        if (coinDetails.isNotEmpty()) {
            coinDetails.forEach { (coin, value) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(coin, modifier = Modifier.weight(1f))
                    Text(value.toString(), modifier = Modifier.weight(1f))
                }
            }
        } else {
            Text("No coins found.")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Input fields for adding new coin details
        Text("Add New Coin", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Input for Coin Name
        BasicTextField(
            value = newCoinKey,
            onValueChange = { newCoinKey = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .border(BorderStroke(2.dp,Color.Magenta))
                        .padding(8.dp)
                ) {
                    if (newCoinKey.isEmpty()) Text("Enter Coin Name", style = MaterialTheme.typography.bodySmall)
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input for Coin Value
        BasicTextField(
            value = newCoinValue,
            onValueChange = { newCoinValue = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .border(BorderStroke(2.dp,Color.Magenta))
                        .padding(8.dp)
                ) {
                    if (newCoinValue.isEmpty()) Text("Enter Coin Value", style = MaterialTheme.typography.bodySmall)
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                // Validate inputs
                val value = newCoinValue.toDoubleOrNull()
                if (newCoinKey.isNotBlank() && value != null) {
                    val updatedDetails = coinDetails.toMutableMap().apply { put(newCoinKey, value) }
                    viewModel.updateCoinDetails(updatedDetails) // Save to DataStore
                    newCoinKey = "" // Clear input fields
                    newCoinValue = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
