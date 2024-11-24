package com.example.dataxstore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun CoinDetailsScreen(viewModel: MainViewModel) {
    // Observe coin details from the ViewModel
    val coinDetails by viewModel.coinDetails.collectAsState(initial = emptyMap())

    // Local UI states for input fields
    var newCoinKey by remember { mutableStateOf("") }
    var newCoinValue by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Title
        Text(
            text = "Coin Details",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display current coins in a LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Occupies remaining space
        ) {
            items(coinDetails.toList()) { (coin, value) ->
                CoinItem(coin = coin, value = value)
            }
        }

        // Input for new coin details
        TextField(
            value = newCoinKey,
            onValueChange = { newCoinKey = it },
            label = { Text("Coin Name") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )
        TextField(
            value = newCoinValue,
            onValueChange = { newCoinValue = it },
            label = { Text("Coin Value") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        // Submit button
        Button(
            onClick = {
                val value = newCoinValue.toDoubleOrNull()
                if (newCoinKey.isNotEmpty() && value != null) {
                    val updatedDetails = coinDetails.toMutableMap().apply { put(newCoinKey, value) }
                    viewModel.updateCoinDetails(updatedDetails)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun CoinItem(coin: String, value: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = coin, style = MaterialTheme.typography.bodyMedium)
        Text(text = value.toString(), style = MaterialTheme.typography.bodySmall)
    }
}
