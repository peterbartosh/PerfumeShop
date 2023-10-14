package com.example.perfumeshop.presentation.features.main.home_feature.search.ui

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.models.Product
import com.example.perfumeshop.data.models.ProductWithAmount
import com.example.perfumeshop.data.repositories.FireRepository
import com.example.perfumeshop.data.repositories.queryToFlow
import com.example.perfumeshop.data.utils.QueryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    //lateinit var currentFlow : Flow<Product>

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    var uploadsAmount by mutableStateOf(0)
    var uploadingMore by mutableStateOf(false)

    var isCashPrice by mutableStateOf(true)

    // search options
    var query = ""
    var queryType = QueryType.brand

    // filter options
    var minValue = 0.0f
    var maxValue = 0.0f
    var volumes = emptyList<String>()
    var isOnHandOnly = false

    // sort options

    var priorities = emptyList<Int>()
    var isAscending = true

    fun clear(){
        super.onCleared()
    }

    override fun onCleared() {
        Log.d("SEARCH_VM_CLEARED", "onCleared: DONE")
        super.onCleared()
    }

    fun sortProducts(priorities : List<Int>, isAscending : Boolean) {
        this.priorities = priorities
        this.isAscending = isAscending

        viewModelScope.launch {
            isFailure = false
            isLoading = true
            isSuccess = false

            Log.d("PRIORODD", "sortProducts: ${priorities.joinToString { it.toString() }}")

            when (priorities.joinToString { it.toString() }){
                "0" -> {
                    Log.d("SORT_TEST", "1 before")
                    searchProducts.sortWith(compareBy { it.product?.cashPrice })
                    Log.d("SORT_TEST", "1 after")
                }
                "1" -> {
                    Log.d("SORT_TEST", "2 before")
                    searchProducts.sortWith(compareBy{ it.product?.volume })
                    Log.d("SORT_TEST", "2 after")
                }
                "01" -> {
                    Log.d("SORT_TEST", "3 before")
                    searchProducts.sortWith(compareBy({ it.product?.cashPrice },{ it.product?.volume }))
                    Log.d("SORT_TEST", "3 after")
                }
                "10" -> {
                    Log.d("SORT_TEST", "4 before")
                    searchProducts.sortWith(compareBy({ it.product?.volume },{ it.product?.cashPrice }))
                    Log.d("SORT_TEST", "4 after")
                }
            }

            if (!isAscending){
                val reversed = searchProducts.reversed()
                searchProducts.clear()
                searchProducts.addAll(reversed)
            }

            delay(50)

            isFailure = false
            isLoading = false
            isSuccess = true

        }
    }

    fun uploadMore() = viewModelScope.launch {
        uploadingMore = true
        uploadsAmount++
        repository.getQueryProducts(
            query = query,
            queryType = queryType,
            N = N,
            uploadsAmount = uploadsAmount
        ).catch {
            Log.d("ERROR_ERROR", "uploadMore: ${it.message}")
        }.collect { product ->
            if (shouldAddProduct(product = product))
                searchProducts.add(ProductWithAmount(
                    product = product,
                    isCashPrice = isCashPrice
                ))
        }
        uploadingMore = false
    }

    fun searchQuery(
        query : String,
        queryType : QueryType = QueryType.brand,
        minValue : Float = 0.0f,
        maxValue : Float = 0.0f,
        isOnHandOnly : Boolean = false,
        volumes : List<String> = emptyList()
    ) {
        this.minValue = minValue
        this.maxValue = maxValue
        this.volumes = volumes
        this.isOnHandOnly = isOnHandOnly

        this.query = query
        this.queryType = queryType

        viewModelScope.launch {

            isFailure = false
            isLoading = true
            isSuccess = false

            uploadsAmount = 0

            searchProducts.clear()

            repository.getQueryProducts(
                query, queryType,
                N = N, uploadsAmount = uploadsAmount
            )
                .catch {
                    Log.d("ERROR_ERROR", "searchQuery: ${it.message}")
                    isFailure = true
                }
                .take(N)
                .collect { product ->
                    if (shouldAddProduct(product = product))
                        searchProducts.add(ProductWithAmount(product = product, isCashPrice = isCashPrice))
                }

            sortProducts(priorities, isAscending)

            isLoading = false

            isSuccess = !isFailure
        }
    }

    private fun shouldAddProduct(product : Product) : Boolean {
        if (minValue != 0.0f || maxValue != 0.0f) {
            val price =
                if (isCashPrice) product.cashPrice else product.cashlessPrice
            if (price != null)
                if (price !in (minValue..maxValue))
                    return false
        }
        if (volumes.isNotEmpty()) {
            val volume = product.volume
            if (volume != null)
                if (volume !in (volumes))
                    return false
        }
        if (isOnHandOnly) {
            val ioh = product.isOnHand
            if (ioh != null && !ioh)
                return false
        }
        return true
    }
}
