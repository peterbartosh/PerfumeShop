package com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui

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
class SearchViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    private var _searchList = MutableStateFlow<MutableList<Product>>(mutableListOf())
    var searchList : StateFlow<List<Product>> = _searchList

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    fun clear(){
        super.onCleared()
    }

    override fun onCleared() {
        Log.d("SEARCH_VM_CLEARED", "onCleared: DONE")
        super.onCleared()
    }

    fun searchQuery(
        query : String,
        queryType : QueryType = QueryType.brand,
        applyFilter : Boolean = false,
        minValue : Float? = null,
        maxValue : Float? = null,
        isOnHand : Boolean? = null,
        volume : List<Int>? = null
    ) = viewModelScope.launch {

        Log.d("SE_QUE_TEST", "searchQuery: DONE")

        isFailure = false
        isLoading = true
        isSuccess = false
        //initSearchQuery = false

        _searchList.value = mutableListOf()

        if (applyFilter)
            repository.getProductsWithFilter(
                minValue = minValue,
                maxValue = maxValue,
                isOnHand = isOnHand,
                volumes = volume
            ).catch {
                    e -> Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                isFailure = true
            }.collect{ product ->
                _searchList.value.add(product)
            }
        else
            repository.getQueryProducts(query, queryType)
                .catch {
                        e -> Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                    isFailure = true
                }.collect { product ->
                    _searchList.value.add(product)
            }

        searchList = _searchList

        Log.d("DATA_TEST", "searchQuery: ${searchList.value}")

        isLoading = false

        if (_searchList.value.isEmpty()) Log.d("EMPTY_EMPTY", "searchQuery: EMPTY")

        if (_searchList.value.isEmpty() || isFailure)
            isFailure = true
        else
            isSuccess = true

        Log.d("DATA_TEST", "searchQuery: ${searchList.value}")

    }
}
