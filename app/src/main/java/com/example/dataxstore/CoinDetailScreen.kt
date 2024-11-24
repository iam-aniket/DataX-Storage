package com.example.dataxstore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
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
    var editMode by remember { mutableStateOf(false) }
    val editableCoinDetails = remember(coinDetails) { coinDetails.toMutableMap() } // Initialize properly

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Title and Edit Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Coin Details",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            IconButton(onClick = {
                editMode = !editMode
                if (!editMode) {
                    // Save changes and exit edit mode
                    viewModel.updateCoinDetails(editableCoinDetails)
                }
            }) {
                Icon(
                    imageVector = if (editMode) Icons.Default.Done else Icons.Default.Edit,
                    contentDescription = if (editMode) "Save Changes" else "Edit"
                )
            }
        }

        // LazyColumn for displaying coins
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(editableCoinDetails.toList()) { (coin, value) ->
                if (editMode) {
                    EditableCoinItem(
                        coin = coin,
                        initialValue = value,
                        onValueChange = { newValue ->
                            editableCoinDetails[coin] = newValue
                        }
                    )
                } else {
                    CoinItem(coin = coin, value = value)
                }
            }
        }

        // Add new coin input
        if (!editMode) {
            var newCoinKey by remember { mutableStateOf("") }
            var newCoinValue by remember { mutableStateOf("") }

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

            Button(
                onClick = {
                    val value = newCoinValue.toDoubleOrNull()
                    if (newCoinKey.isNotEmpty() && value != null) {
                        editableCoinDetails[newCoinKey] = value
                        viewModel.updateCoinDetails(editableCoinDetails) // Persist changes
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun EditableCoinItem(coin: String, initialValue: Double, onValueChange: (Double) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = coin, style = MaterialTheme.typography.bodyMedium)
        var valueText by remember { mutableStateOf(initialValue.toString()) }

        TextField(
            value = valueText,
            onValueChange = { newText ->
                valueText = newText
                val newValue = newText.toDoubleOrNull()
                if (newValue != null) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
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
