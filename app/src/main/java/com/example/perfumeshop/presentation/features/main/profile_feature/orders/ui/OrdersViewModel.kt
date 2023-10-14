package com.example.perfumeshop.presentation.features.main.profile_feature.orders.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.models.Order
import com.example.perfumeshop.data.models.ProductWithAmount
import com.example.perfumeshop.data.repositories.FireRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrdersViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

//    private var _ordersList = MutableStateFlow(mutableListOf<Order>())
//    var ordersList : StateFlow<List<Order>> = _ordersList

    private val _ordersList = mutableListOf<Order>()
    var ordersList = _ordersList.toList()

    private val _productsWithAmountMap = mutableMapOf<String, List<ProductWithAmount>>()
    var productsWithAmountMap = _productsWithAmountMap.toMap()

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)
    //var initOrdersQuery by mutableStateOf(false)

    fun clear(){
        super.onCleared()
    }

    init {
        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
            getUserOrders()
    }

    fun getProductsWithAmount(){
        val productsWithAmount = mutableListOf<ProductWithAmount>()
        viewModelScope.launch {

        }
    }


    private fun getUserOrders() = viewModelScope.launch {
        isSuccess = false
        isFailure = false
        isLoading = true

        repository.getUserOrders()
            .catch {
                    e -> Log.d("ERROR_ERROR", "getUserOrders: ${e.message}")
                isFailure = true
            }.collect { order ->
                _ordersList.add(order)
                val id = order.id
                if (id != null)
                    _productsWithAmountMap[id] = repository.getOrderProducts(id).map { orderProduct ->
                        ProductWithAmount(
                            product = repository.getProduct(orderProduct.productId),
                            amount = orderProduct.amount,
                            isCashPrice = orderProduct.isCashPrice
                        )

                    }
            }

        ordersList = _ordersList
        productsWithAmountMap = _productsWithAmountMap

        isLoading = false

        isSuccess = !isFailure

        // Log.d("VIEW_MODEL_REQ", "searchQuery: REQ ENDED ${_searchList.size}  ${searchList.size}")

    }



}