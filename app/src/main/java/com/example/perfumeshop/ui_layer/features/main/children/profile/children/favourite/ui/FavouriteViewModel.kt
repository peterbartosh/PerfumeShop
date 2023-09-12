package com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.data_layer.utils.QueryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private var _userProducts = MutableStateFlow<MutableList<Product>>(value = mutableListOf())
    var userProducts: StateFlow<List<Product>> = _userProducts


    var isLoading  by mutableStateOf(false)
    var isSuccess  by mutableStateOf(false)
    var isFailure  by mutableStateOf(false)
    //var isInitialized  by mutableStateOf(false)

    //var initSearchQuery by mutableStateOf(true)

    fun clear(){
        super.onCleared()
    }

    fun addToFavourite(product: Product){
        _userProducts.value.add(product)
        userProducts = _userProducts

        //updateInDatabase() lazily todo
    }

    fun removeFromFavourite(productId: String){
        _userProducts.value.removeIf { p -> p.id == productId }
        userProducts = _userProducts

        //updateInDatabase() lazily todo
    }

    fun isInFavourite(productId : String) : Boolean =
        _userProducts.value.find { p -> p.id!! == productId } != null



//    fun searchQuery(query : String, queryType: QueryType) = viewModelScope.launch {
//        isInitialized = true
//        isFailure = false
//        isLoading = true
//        repository.getQueryProducts(query, queryType)
//            .catch {
//                    e -> Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
//                isFailure = true
//            }.collect {
//                _userProducts.value = it.toMutableList()
//            }
//
//        userProducts = _userProducts
//
//        isLoading = false
//
//        if (_userProducts.value.isEmpty()) Log.d("EMPTY_EMPTY", "searchQuery: EMPTY")
//
//        if (_userProducts.value.isEmpty() || isFailure)
//            isFailure = true
//        else
//            isSuccess = true
//
//        // Log.d("VIEW_MODEL_REQ", "searchQuery: REQ ENDED ${_searchList.size}  ${searchList.size}")
//
//    }



}