package com.example.perfumeshop.presentation.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(private val roomRepository: RoomRepository): ViewModel() {

    private val _cartProductsAmount = MutableStateFlow(0)
    val cartProductsAmount: StateFlow<Int> = _cartProductsAmount

    init {
        viewModelScope.launch {
            roomRepository.getCartProductsAmount()
                .catch { e ->
                    Log.d(TAG, "init: $e")
                }
                .collect { amount ->
                    _cartProductsAmount.value = amount
                }
        }
    }
}