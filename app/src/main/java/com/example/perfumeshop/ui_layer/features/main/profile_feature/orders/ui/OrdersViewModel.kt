package com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class OrdersViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private var _ordersList = MutableStateFlow<MutableList<Order>>(mutableListOf())
    var ordersList : StateFlow<List<Order>> = _ordersList

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)
    //var initOrdersQuery by mutableStateOf(false)

    fun clear(){
        super.onCleared()
    }

    init {
        getUserOrders()
    }


    private fun getUserOrders() = viewModelScope.launch {
        isSuccess = false
        isFailure = false
        isLoading = true
       // initOrdersQuery = false
        repository.getUserOrders()
            .catch {
                    e -> Log.d("ERROR_ERROR", "getUserOrders: ${e.message}")
                isFailure = true
            }.collect {
                _ordersList.value.add(it)
            }

        ordersList = _ordersList

        isLoading = false

        if (_ordersList.value.isEmpty()) Log.d("EMPTY_EMPTY", "getUserOrders: EMPTY")

        if (_ordersList.value.isEmpty() || isFailure)
            isFailure = true
        else
            isSuccess = true

        // Log.d("VIEW_MODEL_REQ", "searchQuery: REQ ENDED ${_searchList.size}  ${searchList.size}")

    }



}