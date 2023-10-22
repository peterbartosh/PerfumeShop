package com.example.perfumeshop.presentation.features.main.home_feature.search.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.utils.QueryType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "SearchViewModel"

const val productsAmountPerPage = 100
const val maxProductPrice = 1000.0f

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    var searchProducts = SnapshotStateList<ProductWithAmount>()

    var isLoading by mutableStateOf(false)
    var isSuccess by mutableStateOf(false)
    var isFailure by mutableStateOf(false)

    var uploadsAmount by mutableStateOf(0)
    var uploadingMore by mutableStateOf(false)

    // search options
    var initQuery = ""
    var initQueryType = QueryType.brand

    // filter options
    var minValue = 0.0f
    var maxValue = maxProductPrice
    var isOnHandOnly = false
    var isMaleOnly = false
    var isFemaleOnly = false
    var startsWith = ""

    // sort options

    var priorities = emptyList<Int>()
    var isAscending = true

    fun updateProductAmount(productInd : Int, amount : Int){
        searchProducts[productInd].amount = amount
    }

    fun updateProductCashState(productInd : Int, isCashPrice : Boolean){
        searchProducts[productInd].isCashPrice = isCashPrice
    }

    fun sortProducts(priorities : List<Int>, isAscending : Boolean) {
        this.priorities = priorities
        this.isAscending = isAscending

        viewModelScope.launch {
            isFailure = false
            isLoading = true
            isSuccess = false

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

    fun uploadMore() {
        uploadingMore = true
        uploadsAmount++
        viewModelScope.launch {
            //delay(100)
            repository.getQueryProducts(
                query = initQuery,
                queryType = initQueryType,
                productsPerPage = productsAmountPerPage,
                uploadsAmount = uploadsAmount
            ).catch {
                Log.d("ERROR_ERROR", "uploadMore: ${it.message}")
            }.collect { product ->
                if (shouldAddProduct(product = product))
                    searchProducts.add(
                        ProductWithAmount(
                            product = product,
                            isCashPrice = true
                        )
                    )
            }.let { result ->
                //
            }
                //delay(100)
        }
        uploadingMore = false
    }

    fun searchQuery(
        query : String,
        queryType : QueryType = QueryType.brand,
        minValue : Float = 0.0f,
        maxValue : Float = maxProductPrice,
        isOnHandOnly : Boolean = false,
        isMaleOnly : Boolean = false,
        isFemaleOnly : Boolean = false
    ) {
        this.minValue = minValue
        this.maxValue = maxValue
        this.isOnHandOnly = isOnHandOnly
        this.isMaleOnly = isMaleOnly
        this.isFemaleOnly = isFemaleOnly

        Log.d(TAG, "searchQuery: ${this.initQuery} ${this.initQueryType} $query $queryType")

        if (this.initQueryType != QueryType.brand && queryType == QueryType.brand)
            startsWith = query

        val initQuery = this.initQuery
        val initQueryType = this.initQueryType

        viewModelScope.launch {

            isFailure = false
            isLoading = true
            isSuccess = false

            uploadsAmount = 0

            searchProducts.clear()

            repository.getQueryProducts(
                query = initQuery,
                queryType = initQueryType,
                productsPerPage = productsAmountPerPage,
                uploadsAmount = uploadsAmount
            )
                .catch {
                    Log.d(TAG, "searchQuery: ${it.message}")
                    isFailure = true
                }
                .take(productsAmountPerPage)
                .collect { product ->
                    if (shouldAddProduct(product = product))
                        searchProducts.add(
                            ProductWithAmount(
                                product = product,
                                isCashPrice = true
                            )
                        )
                }.let { result ->
                    //
                }

            sortProducts(priorities, isAscending)

            isLoading = false

            isSuccess = !isFailure
        }
    }

    private fun shouldAddProduct(product : Product) : Boolean {
        if (startsWith.isNotEmpty()){
            var valid = true
            product.brand?.let { brand ->
                valid = brand.startsWith(startsWith, ignoreCase = true)
            }
            if (!valid) return false
        }
        if (minValue != 0.0f || maxValue != maxProductPrice) {
            var valid = true
            product.cashPrice?.let { price ->
                valid = price in (minValue..maxValue)
            }
            product.cashlessPrice?.let { price ->
                valid = price in (minValue..maxValue)
            }
            if (!valid) return false
        }
        if (isMaleOnly) {
            var valid = true
            product.productSex?.let { sex ->
                valid = sex == "Male"
            }
            if (!valid) return false
        }
        if (isFemaleOnly) {
            var valid = true
            product.productSex?.let { sex ->
                valid = sex == "Female"
            }
            if (!valid) return false
        }
        if (isOnHandOnly) {
            var valid = true
            product.isOnHand?.let { ioh ->
                valid = ioh
            }
            if (!valid) return false
        }
        return true
    }
}
