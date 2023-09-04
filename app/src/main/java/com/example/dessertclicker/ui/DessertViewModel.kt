package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DessertUiState())
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    init {
        resetState()
    }

    private fun determineDessertToShow(dessertsSold: Int): Dessert {
        var dessertToShow = dessertList.first()
        for (dessert in dessertList) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }

    fun updateState() {
        val nextDessert = determineDessertToShow(_uiState.value.dessertsSold + 1)

        _uiState.update { currentState ->
            currentState.copy(
                dessertsSold = currentState.dessertsSold + 1,
                revenue = currentState.revenue + currentState.currentDessertPrice,
                currentDessertPrice = nextDessert.price,
                currentDessertImageId = nextDessert.imageId
            )
        }
    }

    fun resetState() {
        // This resets the uiState to the default values in the DessertUiState data class
        _uiState.value = DessertUiState()

        // You can also reset Ui State to specific values using code similar to this
//        val firstDessert = determineDessertToShow(dessertsSold = 0)
//        _uiState.value = DessertUiState(
//            dessertsSold = 0,
//            revenue = 0,
//            currentDessertPrice = firstDessert.price,
//            currentDessertImageId = firstDessert.imageId
//        )
    }
}