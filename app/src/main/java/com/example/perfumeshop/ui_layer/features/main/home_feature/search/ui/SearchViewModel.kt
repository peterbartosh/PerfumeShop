package com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.ProductWithAmount
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.data_layer.repositories.applyFilter
import com.example.perfumeshop.data_layer.repositories.queryToFlow
import com.example.perfumeshop.data_layer.utils.QueryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

const val N = 100
@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    val searchProducts = SnapshotStateList<ProductWithAmount>()

    lateinit var currentFlow : Flow<Product>

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    var uploadsAmount by mutableStateOf(0)
    var uploadingMore by mutableStateOf(false)

    var isCashPrice by mutableStateOf(true)

    fun clear(){
        super.onCleared()
    }

    override fun onCleared() {
        Log.d("SEARCH_VM_CLEARED", "onCleared: DONE")
        super.onCleared()
    }

    fun sortProducts(priorities : List<Int>, isAscending : Boolean = true) = viewModelScope.launch {
        isFailure = false
        isLoading = true
        isSuccess = false

        when (priorities){
            listOf(0) -> { searchProducts.sortWith(compareBy { it.product?.cashPrice }) }
            listOf(1) -> { searchProducts.sortWith(compareBy{ it.product?.volume }) }
            listOf(0,1) -> { searchProducts.sortWith(compareBy({ it.product?.cashPrice },{ it.product?.volume })) }
            listOf(1,0) -> { searchProducts.sortWith(compareBy({ it.product?.volume },{ it.product?.cashPrice })) }
        }

        if (!isAscending) searchProducts.reverse()

        isFailure = false
        isLoading = false
        isSuccess = true
    }

    fun uploadMore() = viewModelScope.launch {
        uploadingMore = true
        uploadsAmount++
        currentFlow.catch {
            Log.d("ERROR_ERROR", "uploadMore: ${it.message}")
        }.drop(uploadsAmount * N).take(N)
            .collectIndexed { ind, product ->
                searchProducts.add(ProductWithAmount(
                    product = product,
                    isCashPrice = isCashPrice
                ))
            Log.d("COLL_TEST_2", "searchQuery: $ind collected")
        }
        uploadingMore = false
    }

    fun searchQuery(
        query : String,
        queryType : QueryType = QueryType.brand,
        applyFilter : Boolean = false,
        minValue : Float? = null,
        maxValue : Float? = null,
        isOnHand : Boolean? = null,
        volumes : List<String>? = null
    ) = viewModelScope.launch {

        isFailure = false
        isLoading = true
        isSuccess = false

        uploadsAmount = 0

        searchProducts.clear()

        val q = repository.getQueryProducts(query, queryType)

        if (applyFilter) q.applyFilter(minValue, maxValue, isOnHand, volumes)

        currentFlow = q.queryToFlow()

        currentFlow
            .catch {
                Log.d("ERROR_ERROR", "searchQuery: ${it.message}")
                isFailure = true
            }
            .take(N)
            .collectIndexed{ ind, product ->
                searchProducts.add(ProductWithAmount(product = product, isCashPrice = isCashPrice))
            }



        isLoading = false

        if (!isFailure) isSuccess = true
    }
}
