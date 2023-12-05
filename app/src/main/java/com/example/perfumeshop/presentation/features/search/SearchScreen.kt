package com.example.perfumeshop.presentation.features.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.ProductType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.UiState
import com.example.perfumeshop.data.utils.rememberSavableMutableStateList
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.Loading
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.ProductsLazyList
import com.example.perfumeshop.presentation.features.search.components.FilterDialog
import com.example.perfumeshop.presentation.features.search.components.SearchField
import com.example.perfumeshop.presentation.features.search.components.SortDialog
import com.example.perfumeshop.presentation.features.search.components.secondary.InputOption
import com.example.perfumeshop.presentation.features.search.components.secondary.SearchOption

typealias SearchRequest = Pair<String, QueryType>

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    //onProductClick: (String) -> Unit,
    initQuery: String,
    initQueryType: QueryType,
) {

    val context = LocalContext.current

    val uiState = searchViewModel.uiState.collectAsState()

    //-------------------------------------------

    // inputs states

    val inputs = rememberSavableMutableStateList(elements = arrayOf<SearchRequest>())

    //-------------------------------------------

    // Product list states

    val showProductList = remember {
        mutableStateOf(true)
    }

    val currentQuery = rememberSaveable {
        mutableStateOf(initQuery)
    }

    val currentQueryType = rememberSaveable {
        mutableStateOf(initQueryType)
    }

    var updateQuery by rememberSaveable(inputs = arrayOf(initQuery, initQueryType)) {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = true){
        if (updateQuery) {

            if (initQueryType.name == QueryType.Type.name && initQuery == ProductType.Compact.name)
                searchViewModel.priorities = listOf(1,2)
            else
                searchViewModel.priorities = listOf(2)

            searchViewModel.initQuery = initQuery
            searchViewModel.initQueryType = initQueryType

            inputs.add(Pair(initQuery, initQueryType))

            searchViewModel.searchQuery(
                curQuery = initQuery,
                curQueryType = initQueryType
            )

            updateQuery = false
        }
    }

    //-------------------------------------------

    // Filtering states

    val showFilter = remember {
        mutableStateOf(false)
    }

    val filterApplied = rememberSaveable {
        mutableStateOf(false)
    }

    //-------------------------------------------

    // Sorting states

    val showSort = remember {
        mutableStateOf(false)
    }

    val sortApplied = rememberSaveable {
        mutableStateOf(false)
    }

    var initQueryState by rememberSaveable {
        mutableStateOf(
            if (initQueryType.name == QueryType.Brand.name)
                initQuery
            else
                ""
        )
    }

    //-------------------------------------------

    // comps

    Column(modifier = Modifier) {

        SearchField(initialQuery = initQueryState) { q ->
            val ind = inputs.indexOfFirst { it.second.name == QueryType.Brand.name }
            if (ind != -1) inputs.removeAt(ind)

            inputs.add(Pair(q, QueryType.Brand))

            currentQuery.value = q
            currentQueryType.value = QueryType.Brand

            searchViewModel.searchQuery(
                curQuery = q,
                curQueryType = QueryType.Brand
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            SearchOption(
                applied = filterApplied.value,
                text = "Фильтр",
                iconId = R.drawable.filter,
                showDialogState = showFilter
            )


            SearchOption(
                applied = sortApplied.value,
                text = "Сортировка",
                iconId = R.drawable.sort,
                showDialogState = showSort
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(start = 15.dp)
        ) {

            inputs.forEachIndexed { ind, req ->
                InputOption(ind = ind, query = req.first, queryType = req.second) {
                    initQueryState = ""
                    inputs.remove(req)
                    currentQuery.value = initQuery
                    currentQueryType.value = initQueryType
                    searchViewModel.searchQuery(
                        curQuery = currentQuery.value,
                        curQueryType = currentQueryType.value
                    )
                }
            }
        }

        if (showProductList.value) {

            when (uiState.value){

                is UiState.Success ->
                    if (searchViewModel.searchProducts.isNotEmpty()) {

                        ProductsLazyList(
                            //onProductClick = onProductClick,
                            listOfProductsWithAmounts = searchViewModel.searchProducts,

                            onAddToFavouriteClick = searchViewModel.favouriteFunctionality::addProduct,
                            onAddToCartClick = searchViewModel.cartFunctionality::addProduct,

                            onRemoveFromFavouriteClick = searchViewModel.favouriteFunctionality::removeProduct,
                            onRemoveFromCartClick = searchViewModel.cartFunctionality::removeProduct,

                            isInFavouriteCheck = searchViewModel.favouriteFunctionality::isInFavourites,
                            isInCartCheck = searchViewModel.cartFunctionality::isInCart,

//                            onAmountChanged = searchViewModel::updateProductAmount,
//                            onCashStateChanged = searchViewModel::updateProductCashState,

                            uploadMoreData = { searchViewModel.uploadMore(currentQuery.value, currentQueryType.value) }
                        )
                    }
                    else
                        NothingFound()

                is UiState.Failure ->
                    ErrorOccurred()
                is UiState.Loading ->
                    Loading()
                else -> Box{}
            }
        }


        if (showFilter.value){

            FilterDialog(
                showDialog = showFilter,
                minInitValue = searchViewModel.minValue.toString(),
                maxInitValue = searchViewModel.maxValue.toString(),
                isOnHandOnlyInitValue = searchViewModel.isOnHandOnly,
                availableVolumes = if (initQueryType.name == QueryType.Type.name)
                    ProductType.valueOf(initQuery).getVolumes()
                else
                    emptyList(),
                isCompactQueryType = initQueryType.name == QueryType.Type.name &&
                        initQuery == ProductType.Compact.name,
                volumesStatesInitValue = searchViewModel.volumes,
                //isMaleOnlyInitValue = searchViewModel.isMaleOnly,
                // isFemaleOnlyInitValue = searchViewModel.isFemaleOnly,
                onApplyButtonClick = {
                        moreThan, lessThan, isOnHand, volumesStates, filterAppliedParam ->
                    //isMaleOnly, isFemaleOnly

                    showFilter.value = false

                    try {
                        val minValue = moreThan.trim().replace(",", ".").toFloat()
                        val maxValue = lessThan.trim().replace(",", ".").toFloat()

                        searchViewModel.searchQuery(
                            newFilterApplied = true,
                            curQuery = currentQuery.value,
                            curQueryType = currentQueryType.value,
                            minValueParam = minValue,
                            maxValueParam = maxValue,
                            isOnHandOnlyParam = isOnHand,
                            volumesParam = volumesStates
//                            isMaleOnly = isMaleOnly,
//                            isFemaleOnly = isFemaleOnly
                        )
                    } catch (e: Exception) {
                        context.showToast(R.string.incorrect_input_error)
                    }

                    filterApplied.value = filterAppliedParam
                }
            )
    }

        if (showSort.value)
            SortDialog(
                showDialog = showSort,
                sortPrioritiesInitValue = searchViewModel.priorities,
                isAscendingInitValue = searchViewModel.isAscending,
                onApplyButtonClick = { isAscending, priorities, sortingApplied ->
                    showSort.value = false
                    searchViewModel.searchQuery(
                        newSortingApplied = true,
                        curQuery = currentQuery.value,
                        curQueryType = currentQueryType.value,
                        prioritiesParam = priorities,
                        isAscendingParam = isAscending
                    )
                    sortApplied.value = sortingApplied
                }
            )
    }
}

