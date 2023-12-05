package com.example.perfumeshop.presentation.features.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.repository.TAG
import com.example.perfumeshop.data.room.entities.toProductWithAmount
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.skeleton.FavouriteFunctionality
import com.example.perfumeshop.data.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Implements cart functionality, which needed throughout the application.
That's why it is scoped to an App() composable.
 */

@HiltViewModel
class CartViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val cartFunctionality: CartFunctionality,
    val favouriteFunctionality: FavouriteFunctionality,
    private val roomRepository: RoomRepository
    ) : ViewModel() {

    private val _userProducts = MutableStateFlow(listOf<ProductWithAmount>())
    val userProducts: StateFlow<List<ProductWithAmount>> = _userProducts

    private var _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState : StateFlow<UiState> = _uiState

    override fun onCleared() {
        Log.d("VM_LFC_TAG", "cart vm cleared")
        super.onCleared()
    }

    init {
        Log.d("VM_LFC_TAG", "cart vm started")
        viewModelScope.launch {
            _uiState.value = UiState.Loading()
            roomRepository.getCartProducts()
                .catch { e ->
                    Log.d(TAG, "cart init: $e")
                    _uiState.value = UiState.Success()
                }.collect { cart ->
                    _uiState.value = UiState.Loading()
                    _userProducts.value = cart.map { cartEntity ->
                        cartEntity.toProductWithAmount()
                    }
                    _uiState.value = UiState.Success()
                }
        }
    }
}