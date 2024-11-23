package com.example.dataxstore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataStoreRepository) : ViewModel() {

    private val _coinDetails = MutableStateFlow<MutableMap<String, Double>>(mutableMapOf())
    val coinDetails: StateFlow<MutableMap<String, Double>> = _coinDetails

    init {
        loadCoinDetails()
    }

    private fun loadCoinDetails() {
        viewModelScope.launch {
            repository.getCoinDetails().collect { details ->
                _coinDetails.value = details
            }
        }
    }

    fun updateCoinDetails(newDetails: MutableMap<String, Double>) {
        viewModelScope.launch {
            repository.updateCoinDetails(newDetails)
            _coinDetails.value = newDetails
        }
    }
}
