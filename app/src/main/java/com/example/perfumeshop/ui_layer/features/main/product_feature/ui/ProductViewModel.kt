package com.example.perfumeshop.ui_layer.features.main.product_feature.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ProductViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {
    private var _productReviews = MutableStateFlow<MutableList<Review>>(mutableListOf())
    var productReviews: StateFlow<List<Review>> = _productReviews

    var product : Product? = null

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    fun clear(){
        super.onCleared()
    }

    fun loadProduct(productId: String) = viewModelScope.launch {
        product = repository.getProduct(productId)
        loadProductReviews(productId = productId)
    }

    private fun loadProductReviews(productId : String) = viewModelScope.launch {
        isFailure = false
        isLoading = true
        repository.getProductReviews(productId).catch {
                    e -> Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                isFailure = true
            }.collect { review ->
                _productReviews.value.add(review)
            }

        productReviews = _productReviews

        isLoading = false


        if (_productReviews.value.isEmpty() || isFailure)
            isFailure = true
        else
            isSuccess = true


    }
}