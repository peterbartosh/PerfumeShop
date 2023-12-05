package com.example.perfumeshop.presentation.features.orders

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.skeleton.FavouriteFunctionality
import com.example.perfumeshop.data.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "OrdersViewModel"

@HiltViewModel
class OrdersViewModel @Inject constructor(
    auth: FirebaseAuth,
    val cartFunctionality: CartFunctionality,
    val favouriteFunctionality: FavouriteFunctionality,
    private val fireRepository: FireRepository
) : ViewModel() {

    private val _ordersList = mutableListOf<Order>()
    var ordersList = _ordersList.toList()

    private val _productsWithAmountMap = mutableMapOf<String, List<ProductWithAmount>>()
    var productsWithAmountMap = _productsWithAmountMap.toMap()

    private var _uiState = MutableStateFlow<UiState>(UiState.Success())
    val uiState : StateFlow<UiState> = _uiState

    init {
        if (auth.currentUser?.isAnonymous == false)
            getUserOrders()
    }

    private fun getUserOrders() = viewModelScope.launch {
        _uiState.value = UiState.Loading()
        val deferred = async {
            fireRepository.getUserOrders()
                .catch { e ->
                    Log.d(TAG, "getUserOrders: ${e.message}")
                    _uiState.value = UiState.Failure(e)
                }.collect { order ->
                    _ordersList.add(order)
                }
        }

        deferred.await()

        val deferreds = mutableListOf<Deferred<Unit>>()

        _ordersList.forEach { order ->
            deferreds.add(
                async {
                    order.id?.let { id ->
                        _productsWithAmountMap[id] =
                            fireRepository.getOrderProducts(id).map { orderProduct ->
                                ProductWithAmount(
                                    product = fireRepository.getProduct(orderProduct.productId),
                                    cashPriceAmount = orderProduct.cashPriceAmount,
                                    cashlessPriceAmount = orderProduct.cashlessPriceAmount
                                )
                            }
                    }
                    Unit
                }
            )
        }

        deferreds.awaitAll()

        _ordersList.sortByDescending { it.date }

        ordersList = _ordersList
        productsWithAmountMap = _productsWithAmountMap

        if (_uiState.value is UiState.Loading)
            _uiState.value = UiState.Success()

    }
}