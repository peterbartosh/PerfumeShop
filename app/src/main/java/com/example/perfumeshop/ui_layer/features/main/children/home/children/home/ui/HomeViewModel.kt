package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.data_layer.utils.QueryType
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {



//    private var _products = MutableStateFlow<MutableList<Product>>(value = mutableListOf())
//    val products: StateFlow<List<Product>> = _products
//
//    var isLoading by mutableStateOf(false)
//    var isSuccess by mutableStateOf(false)
//    var isFailure by mutableStateOf(false)
//
//    fun getBest10RatingProducts(){
//
//    }

//    init {
//        loadUserProducts()
//    }

//    private fun loadUserProducts() = viewModelScope.launch {
//        isLoading = true
//
//
//        repository.getAllProductsFromDatabase()
//            .catch {
//                    e -> Log.d("ERROR", "loadUserProducts: ${e.message}")
//                isFailure = true
//            }.collect {
//                _products.value.add(it)
//            }
//        isLoading = false
//
//        if (_products.value.isEmpty() || isFailure)
//            isFailure = true
//        else
//            isSuccess = true
//
//    }
//
//    fun getColl(st : String){
//
//    }
//
//    private suspend fun getBrandCollection(productBrandCollection: String) : List<Product>  = coroutineScope{
//
//        withContext(Dispatchers.Default) {
//            _products.value.filter { product ->
//                product.productBrandCollection == productBrandCollection
//            }
//        }
//
//    }

}
