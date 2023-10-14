package com.example.perfumeshop.presentation.features.main.home_feature.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.models.Product
import com.example.perfumeshop.data.models.ProductWithAmount
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel


@Composable
fun SearchScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    searchViewModel: SearchViewModel,
    onProductClick: (String) -> Unit,
    query: String,
    queryType: QueryType,
) {

    //-------------------------------------------

    // Product list states

    val context = LocalContext.current

    val showProductList = remember {
        mutableStateOf(true)
    }

    val showPriceType = remember {
        mutableStateOf(false)
    }

    val isCashPriceState = rememberSaveable {
        mutableStateOf(true)
    }

    var updateQuery by rememberSaveable(inputs = arrayOf(query, queryType)) {
        mutableStateOf(true)
    }

    if (updateQuery) {
        searchViewModel.searchQuery(query = query, queryType = queryType)
        updateQuery = false
    }

    //-------------------------------------------

    // Filtering states

    val showFilter = remember {
        mutableStateOf(false)
    }

    val volumes = listOf("35", "50", "125")

    val filterApplied = remember {
        mutableStateOf(false)
    }

    //-------------------------------------------

    // Sorting states

    val showSort = remember {
        mutableStateOf(false)
    }

    val sortApplied = remember {
        mutableStateOf(false)
    }

    //-------------------------------------------

    // comps

    Column(modifier = Modifier) {

        SearchForm(initialQuery = query) { q ->
            searchViewModel.searchQuery(query = q, queryType = QueryType.brand)
        }

        Divider()
        
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            SearchOption(applied = filterApplied.value, text = "Фильтр", iconId = R.drawable.filter, showState = showFilter)

            Box {
                SearchOption(applied = !isCashPriceState.value, text = "Цена", iconId = R.drawable.money, showState = showPriceType)

                PriceDropDownMenu(
                    expanded = showPriceType,
                    selected = isCashPriceState
                )
            }

            SearchOption(applied = sortApplied.value, text = "Сортировка", iconId = R.drawable.sort, showState = showSort)
        }

        Divider()

        Spacer(modifier = Modifier.height(5.dp))

        ShowProductList(
            showProductList = showProductList,
            searchViewModel = searchViewModel,
            onProductClick = onProductClick,
            onAddToCartClick = cartViewModel::addToCart,
            onAddToFavouriteClick = favouriteViewModel::addToFavourite,
            onRemoveFromCartClick = cartViewModel::removeFromCart,
            onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
            isInFavouriteCheck = favouriteViewModel::isInFavourite,
            isCashPriceState = isCashPriceState,
            isInCartCheck = cartViewModel::isInCart
        )


        if (showFilter.value)
            FilterDialog(
                showDialog = showFilter,
                minInitValue = searchViewModel.minValue.toString(),
                maxInitValue = searchViewModel.maxValue.toString(),
                isOnHandOnlyInitValue = searchViewModel.isOnHandOnly,
                volumesStatesInitValue = List(volumes.size) { ind -> searchViewModel.volumes.contains(volumes[ind]) },
                volumes = volumes,
                onApplyButtonClick = { moreThan, lessThan, isOnHand, states ->
                    showFilter.value = false

                    try {
                        val minValue = moreThan.toFloat()
                        val maxValue = lessThan.toFloat()

                        searchViewModel.searchQuery(
                            query = query,
                            queryType = queryType,
                            minValue = minValue,
                            maxValue = maxValue,
                            isOnHandOnly = isOnHand,
                            volumes = volumes.filterIndexed{ ind, _ -> states[ind] }
                        )
                    } catch (e: Exception) {
                        showToast(context = context, "Ошибка\nНекорректные данные")
                    }

                    filterApplied.value = true
                },
                onClearClick = {
                    searchViewModel.minValue = 0.0f
                    searchViewModel.maxValue = 0.0f
                    searchViewModel.isOnHandOnly = false
                    searchViewModel.volumes = emptyList()

                    filterApplied.value = false
                }
            )

        if (showSort.value)
            SortDialog(
                showDialog = showSort,
                sortPrioritiesInitValue = searchViewModel.priorities,
                isAscendingInitValue = searchViewModel.isAscending,
                onApplyButtonClick = { isAscending, priorities ->
                    showSort.value = false
                    searchViewModel.sortProducts(
                        priorities = priorities,
                        isAscending = isAscending
                    )
                    sortApplied.value = true
                },
                onClearClick = {
                    searchViewModel.priorities = emptyList()
                    searchViewModel.isAscending = true
                    sortApplied.value = false
                }
            )

    }
}

@Composable
fun ShowProductList(
    showProductList: MutableState<Boolean>,
    searchViewModel: SearchViewModel,
    onProductClick: (String) -> Unit,
    onAddToFavouriteClick: (Product) -> Unit,
    onAddToCartClick: (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick: (String) -> Unit,
    onRemoveFromCartClick: (String) -> Unit,
    isInFavouriteCheck: (String) -> Boolean,
    isInCartCheck: (String) -> Boolean,
    isCashPriceState: MutableState<Boolean>
) {

    remember(isCashPriceState.value) {
        searchViewModel.isCashPrice = isCashPriceState.value
        searchViewModel.searchProducts.forEach{ it.isCashPrice = isCashPriceState.value }
        null
    }

    if (showProductList.value) {
        if (searchViewModel.isSuccess) {
            LazyProductList(
                parentScreen = "search",
                onProductClick = onProductClick,
                listOfProductsWithAmounts = searchViewModel.searchProducts,
                updateChangedAmount = { ind, amount -> searchViewModel.searchProducts[ind].amount = amount },
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                isCashPriceState = isCashPriceState,
                isInCartCheck = isInCartCheck,
                UploadMoreButton = { UploadMoreButton(searchViewModel = searchViewModel) }
            )
        }

        if (searchViewModel.isFailure)
            Text(text = stringResource(id = R.string.error_occured))
        else if (searchViewModel.isLoading)
            LoadingIndicator()
        else if (!searchViewModel.isLoading && searchViewModel.searchProducts.isEmpty())
            Text(text = stringResource(id = R.string.nothing_found))
    }
}

