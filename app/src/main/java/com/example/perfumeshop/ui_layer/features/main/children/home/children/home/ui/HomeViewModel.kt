package com.example.perfumeshop.ui_layer.features.main.children.home.children.home.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private val _hotListOfProducts = MutableStateFlow<MutableList<Product>>(mutableListOf())
    var hotListOfProducts : StateFlow<List<Product>> = _hotListOfProducts

    var isSuccess by mutableStateOf(false)

    // other lists

    init {
        loadHotListOfProduct()
    }

    private fun loadHotListOfProduct() = viewModelScope.launch {
        Log.d("HOT_LIST_LOAD", "loadHotListOfProduct: DONE")
        repository.getHotCollection()
            .catch {
                Log.d("ERROR_ERROR", "loadHotListOfProduct: ${it.message}")
            }
            .collect{ _hotListOfProducts.value.add(it)  }
        hotListOfProducts = _hotListOfProducts
        isSuccess = true
    }

    override fun onCleared() {
        Log.d("HOME_VM", "CLEARED")
        super.onCleared()
    }




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
