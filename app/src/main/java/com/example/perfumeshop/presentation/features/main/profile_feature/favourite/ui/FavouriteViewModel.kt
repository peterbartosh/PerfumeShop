package com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.room.entities.toProductWithAmount
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.skeleton.FavouriteFunctionality
import com.example.perfumeshop.data.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
Implements favourites functionality, which needed throughout the application.
That's why it is scoped to an App() composable.
 */

const val TAG = "FavouriteViewModel"

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    val cartFunctionality: CartFunctionality,
    val favouriteFunctionality: FavouriteFunctionality,
    private val roomRepository: RoomRepository,
) : ViewModel() {

    private val _userProducts = MutableStateFlow(listOf<ProductWithAmount>())
    val userProducts: StateFlow<List<ProductWithAmount>> = _userProducts

    private var _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState : StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = UiState.Loading()
            roomRepository.getCartProducts()
                .catch { e ->
                    Log.d(com.example.perfumeshop.data.repository.TAG, "cart init: $e")
                    _uiState.value = UiState.Success()
                }.collect { cart ->
                    _uiState.value = UiState.Loading()
                    _userProducts.value = cart.map { favEntity ->
                        favEntity.toProductWithAmount()
                    }
                    _uiState.value = UiState.Success()
                }
        }
    }
}