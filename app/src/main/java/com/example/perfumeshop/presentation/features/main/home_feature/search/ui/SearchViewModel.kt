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
import com.example.perfumeshop.data.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "SearchViewModel"

const val productsAmountPerPage = 100
const val maxProductPrice = 1000.0f

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: FireRepository) : ViewModel() {

    // if content changes, triggers recomposition.
    var searchProducts = SnapshotStateList<ProductWithAmount>()

    private var _uiState = MutableStateFlow<UiState>(UiState.NotStarted())
    val uiState : StateFlow<UiState> = _uiState

    var uploadsAmount by mutableStateOf(0)
    var uploadingMore by mutableStateOf(false)

    // search options
    var initQuery = ""
    var initQueryType = QueryType.brand

    // filter options
    var minValue = 0.0f
    var maxValue = maxProductPrice
    var isOnHandOnly = false
    var volumes = emptyList<Double>()
//    var isMaleOnly = false
//    var isFemaleOnly = false
    //var startsWith = ""

    // sort options

    var priorities = emptyList<Int>()
    var isAscending = true

//    fun updateProductAmount(productInd : Int, amount : Int){
//        searchProducts[productInd].amount = amount
//    }
//
//    fun updateProductCashState(productInd : Int, isCashPrice : Boolean){
//        searchProducts[productInd].isCashPrice = isCashPrice
//    }

    fun uploadMore(currentQuery: String, currentQueryType: QueryType) {
        uploadingMore = true
        uploadsAmount++
        viewModelScope.launch {

            repository.getQueryProducts(
                initQuery = initQuery,
                initQueryType = initQueryType,
                query = currentQuery,
                queryType = currentQueryType,
                productsPerPage = productsAmountPerPage,
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
        curQueryType: QueryType = QueryType.brand,
        newFilterApplied: Boolean = false,
        newSortingApplied: Boolean = false,
        minValueParam: Float = 0.0f,
        maxValueParam: Float = maxProductPrice,
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

            repository.constructFilter(
//                minValue = minValue,
//                maxValue = maxValue,
                isOnHandOnly = isOnHandOnly,
                volumes = volumes
            )

            repository.getQueryProducts(
                initQuery = initQuery,
                initQueryType = initQueryType,
                query = curQuery,
                queryType = curQueryType,
                productsPerPage = productsAmountPerPage,
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


    private fun sortProducts(
        priorities : List<Int>,
        isAscending : Boolean
    ) {
        this.priorities = priorities
        this.isAscending = isAscending

        viewModelScope.launch {
            _uiState.value = UiState.Loading()

            when (priorities.joinToString { it.toString() }) {
                "0" -> searchProducts.sortWith( compareBy { it.product?.cashPrice })
                "1" -> searchProducts.sortWith( compareBy { it.product?.volume })
                "01" -> searchProducts.sortWith( compareBy({ it.product?.cashPrice },{ it.product?.volume }))
                "10" -> searchProducts.sortWith( compareBy({ it.product?.volume },{ it.product?.cashPrice }))
                else -> {}
            }

            if (!isAscending){
                val reversed = searchProducts.reversed()
                searchProducts.clear()
                searchProducts.addAll(reversed)
            }

            _uiState.value = UiState.Success()
        }
    }

    private fun priceFilter(product: Product) : Boolean {
        if (minValue != 0.0f || maxValue != maxProductPrice) {
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

//    private fun shouldAddProduct(product : Product) : Boolean {
//        if (startsWith.isNotEmpty()){
//            var valid = false
//            product.brand?.let { brand ->
//                valid = brand.startsWith(startsWith, ignoreCase = true)
//            }
//            if (!valid) return false
//        }
//        if (minValue != 0.0f || maxValue != maxProductPrice) {
//            var valid = false
//            product.cashPrice?.let { price ->
//                valid = price in (minValue..maxValue)
//            }
//            product.cashlessPrice?.let { price ->
//                valid = valid || price in (minValue..maxValue)
//            }
//            if (!valid) return false
//        }
//        if (volumes.isNotEmpty()){
//            var valid = false
//            product.volume?.let { volume ->
//                valid = volume in volumes
//            }
//            if (!valid) return false
//        }
//        if (isOnHandOnly) {
//            var valid = false
//            product.isOnHand?.let { ioh ->
//                valid = ioh
//            }
//            if (!valid) return false
//        }
////        if (isMaleOnly) {
////            var valid = true
////            product.productSex?.let { sex ->
////                valid = sex == "Male"
////            }
////            if (!valid) return false
////        }
////        if (isFemaleOnly) {
////            var valid = true
////            product.productSex?.let { sex ->
////                valid = sex == "Female"
////            }
////            if (!valid) return false
////        }
//        return true
//    }
}
