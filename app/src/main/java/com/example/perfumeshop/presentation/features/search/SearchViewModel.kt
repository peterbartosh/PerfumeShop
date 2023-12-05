package com.example.perfumeshop.presentation.features.search

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
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.skeleton.FavouriteFunctionality
import com.example.perfumeshop.data.utils.Constants
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "SearchViewModel"


@HiltViewModel
class SearchViewModel @Inject constructor(
    val cartFunctionality: CartFunctionality,
    val favouriteFunctionality: FavouriteFunctionality,
    private val fireRepository: FireRepository
) : ViewModel() {

    // if content changes, triggers recomposition.
    var searchProducts = SnapshotStateList<ProductWithAmount>()

    private var _uiState = MutableStateFlow<UiState>(UiState.NotStarted())
    val uiState : StateFlow<UiState> = _uiState

    var uploadsAmount by mutableStateOf(0)
    var uploadingMore by mutableStateOf(false)

    // search options
    var initQuery = ""
    var initQueryType = QueryType.Brand

    // filter options
    var minValue = 0.0f
    var maxValue = Constants.MAX_PRODUCT_PRICE
    var isOnHandOnly = false
    var volumes = emptyList<Double>()
//    var isMaleOnly = false
//    var isFemaleOnly = false
    //var startsWith = ""

    // sort options

    var priorities = emptyList<Int>()
    var isAscending = true

    fun uploadMore(currentQuery: String, currentQueryType: QueryType) {
        uploadingMore = true
        uploadsAmount++
        viewModelScope.launch {

            fireRepository.getQueryProducts(
                initQuery = initQuery,
                initQueryType = initQueryType,
                query = currentQuery,
                queryType = currentQueryType,
                productsPerPage = Constants.ITEMS_PER_PAGE_AMOUNT,
                uploadsAmount = uploadsAmount,
                isAscending = isAscending,
                priorities = priorities
            ).catch {
                Log.d("ERROR_ERROR", "uploadMore: ${it.message}")
            }.collect { product ->
                if (priceFilter(product))
                    searchProducts.add(
                        ProductWithAmount(product = product)
                    )
            }.let { result ->
                //
            }
                //delay(100)
        }
        uploadingMore = false
    }

    fun searchQuery(
        curQuery: String,
        curQueryType: QueryType = QueryType.Brand,
        newFilterApplied: Boolean = false,
        newSortingApplied: Boolean = false,
        minValueParam: Float = 0.0f,
        maxValueParam: Float = Constants.MAX_PRODUCT_PRICE,
        isOnHandOnlyParam: Boolean = false,
        volumesParam: List<Double> = emptyList(),
        prioritiesParam: List<Int> = emptyList(),
        isAscendingParam: Boolean = true
//        isMaleOnly : Boolean = false,
//        isFemaleOnly : Boolean = false
    ) {

        if (newFilterApplied) {
            this.minValue = minValueParam
            this.maxValue = maxValueParam
            this.isOnHandOnly = isOnHandOnlyParam
            this.volumes = volumesParam
        }

        if (newSortingApplied){
            this.priorities = prioritiesParam
            this.isAscending = isAscendingParam
        }
//        this.isMaleOnly = isMaleOnly
//        this.isFemaleOnly = isFemaleOnly

        Log.d(TAG, "searchQuery: ${this.initQuery} ${this.initQueryType} $curQuery $curQueryType")

//        if (this.initQueryType != QueryType.brand && curQueryType == QueryType.brand)
//            startsWith = curQuery

        val initQuery = this.initQuery
        val initQueryType = this.initQueryType

        viewModelScope.launch {

            _uiState.value = UiState.Loading()

            uploadsAmount = 0

            searchProducts.clear()

            fireRepository.constructFilter(
//                minValue = minValue,
//                maxValue = maxValue,
                isOnHandOnly = isOnHandOnly,
                volumes = volumes
            )

            fireRepository.getQueryProducts(
                initQuery = initQuery,
                initQueryType = initQueryType,
                query = curQuery,
                queryType = curQueryType,
                productsPerPage = Constants.ITEMS_PER_PAGE_AMOUNT,
                uploadsAmount = uploadsAmount,
                isAscending = isAscending,
                priorities = priorities
            )
                .catch { e ->
                    Log.d(TAG, "searchQuery: ${e.message}")
                    _uiState.value = UiState.Failure(e)
                }
                //.take(productsAmountPerPage)
                .collect { product ->
                    Log.d("PROHDUI", "searchQuery: ${product.id}")

                    if (priceFilter(product))
                        searchProducts.add(ProductWithAmount(product = product))
                }.let { result ->
                    //
                }

            sortProducts(priorities, isAscending)

            if (_uiState.value is UiState.Loading) _uiState.value = UiState.Success()
        }
    }

    private fun createSelectors(priorities: List<Int>): List<(ProductWithAmount) -> Comparable<*>?>{
       return priorities.map {
           when (it) {
               0 -> { productWithAmount: ProductWithAmount ->
                   productWithAmount.product?.cashPrice
               }
               1 -> { productWithAmount: ProductWithAmount ->
                   productWithAmount.product?.volume
               }
               2 -> { productWithAmount: ProductWithAmount ->
                   productWithAmount.product?.brand
               }
               else -> { productWithAmount: ProductWithAmount ->
                   productWithAmount.product?.id
               }
           }
       }
    }

    private fun sortProducts(
        priorities : List<Int>,
        isAscending : Boolean
    ) {
        this.priorities = priorities
        this.isAscending = isAscending

        viewModelScope.launch {
            _uiState.value = UiState.Loading()

            searchProducts.sortWith(compareBy(*createSelectors(priorities).toTypedArray()))

            if (!isAscending){
                val reversed = searchProducts.reversed()
                searchProducts.clear()
                searchProducts.addAll(reversed)
            }

            _uiState.value = UiState.Success()
        }
    }

    private fun priceFilter(product: Product) : Boolean {
        if (minValue != 0.0f || maxValue != Constants.MAX_PRODUCT_PRICE) {
            var valid = false
            product.cashPrice?.let { price ->
                valid = price in (minValue..maxValue)
            }
            product.cashlessPrice?.let { price ->
                valid = valid || price in (minValue..maxValue)
            }
            return valid
        }
        return true
    }

}
