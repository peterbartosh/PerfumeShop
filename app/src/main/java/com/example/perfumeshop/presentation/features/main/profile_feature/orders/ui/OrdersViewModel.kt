package com.example.perfumeshop.presentation.features.main.profile_feature.orders.ui

import android.nfc.Tag
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.Order
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "OrdersViewModel"

@HiltViewModel
class OrdersViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private val _ordersList = mutableListOf<Order>()
    var ordersList = _ordersList.toList()

    private val _productsWithAmountMap = mutableMapOf<String, List<ProductWithAmount>>()
    var productsWithAmountMap = _productsWithAmountMap.toMap()

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    init {
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
            getUserOrders()
    }

    private fun getUserOrders() = viewModelScope.launch {
        isSuccess = false
        isFailure = false
        isLoading = true

        val deferred = async {
            repository.getUserOrders()
                .catch { e ->
                    Log.d(TAG, "getUserOrders: ${e.message}")
                    isFailure = true
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
                            repository.getOrderProducts(id).map { orderProduct ->
                                ProductWithAmount(
                                    product = repository.getProduct(orderProduct.productId),
                                    amount = orderProduct.amount,
                                    isCashPrice = orderProduct.isCashPrice
                                )
                            }
                    }
                    Unit
                }
            )
        }

        deferreds.awaitAll()

        ordersList = _ordersList
        productsWithAmountMap = _productsWithAmountMap

        isLoading = false

        isSuccess = !isFailure

    }
}