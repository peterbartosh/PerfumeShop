package com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui

import android.util.Log
import androidx.compose.runtime.MutableState
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

    private var _searchList = MutableStateFlow<List<Product>>(emptyList())
    var searchList : StateFlow<List<Product>> = _searchList

    lateinit var query : MutableState<String>
    lateinit var queryType : MutableState<QueryType>

    fun updateQuery(query: String = "", queryType: QueryType = QueryType.brand){
        this.query.value = query
        this.queryType.value = queryType
    }

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)
    var isInitialized by mutableStateOf(false)

    var initSearchQuery by mutableStateOf(true)

    fun clear(){
        super.onCleared()
    }


    fun searchQuery() = viewModelScope.launch {
        isInitialized = true
        isFailure = false
        isLoading = true
        repository.getQueryProducts(query.value, queryType.value)
            .catch {
                    e -> Log.d("ERROR_ERROR", "searchQuery: ${e.message}")
                isFailure = true
            }.collect {
                _searchList.value = it
            }

        searchList = _searchList

        isLoading = false

        if (_searchList.value.isEmpty()) Log.d("EMPTY_EMPTY", "searchQuery: EMPTY")

        if (_searchList.value.isEmpty() || isFailure)
            isFailure = true
        else
            isSuccess = true

       // Log.d("VIEW_MODEL_REQ", "searchQuery: REQ ENDED ${_searchList.size}  ${searchList.size}")

    }



}