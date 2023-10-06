package com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.mail.EmailSender
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderMakingViewModel @Inject constructor(private val repository: FireRepository,
                                     private val emailSender: EmailSender) : ViewModel(){

    val isLoading : MutableState<Boolean> = mutableStateOf(false)
    val isSuccess : MutableState<Boolean> = mutableStateOf(false)
    //val e : MutableState<Exception?> = mutableStateOf(null)

    fun confirmOrder(order : Order, products: List<Pair<Product, Int>>) {
        viewModelScope.launch {
            isLoading.value = true
            val saveTaskState = emailSender.sendOrderEmail(order = order, products = products)
            val sendTaskState = repository.saveToFirebase(item = order, collectionName = "orders")
            isSuccess.value = saveTaskState.first && sendTaskState.first
            if (isSuccess.value)
                Log.d("ERROR_ERROR", (saveTaskState.second ?: sendTaskState.second).toString())
        }
    }
}