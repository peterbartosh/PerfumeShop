package com.example.perfumeshop.presentation.features.main.home_feature.search.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
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
import com.example.perfumeshop.data.model.ProductWithAmount
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.presentation.components.ErrorOccurred
import com.example.perfumeshop.presentation.components.LazyProductList
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.components.NothingFound
import com.example.perfumeshop.presentation.components.showToast
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel
import kotlinx.coroutines.Job


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

    var updateQuery by rememberSaveable(inputs = arrayOf(query, queryType)) {
        mutableStateOf(true)
    }

    if (updateQuery) {
        searchViewModel.initQuery = query
        searchViewModel.initQueryType = queryType
        searchViewModel.searchQuery(query = query, queryType = queryType)
        updateQuery = false
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

    //-------------------------------------------

    // comps

    Column(modifier = Modifier) {

        SearchForm(initialQuery =
                   if (queryType == QueryType.brand)
                       query
                   else
                       ""
        ) { q ->
            searchViewModel.searchQuery(query = q, queryType = QueryType.brand)
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            SearchOption(applied = filterApplied.value, text = "Фильтр", iconId = R.drawable.filter, showDialogState = showFilter)

//            Box {
//                SearchOption(applied = !isCashPriceState.value, text = "Цена", iconId = R.drawable.money, showState = showPriceType)
//
//                PriceDropDownMenu(
//                    expanded = showPriceType,
//                    selected = isCashPriceState
//                )
//            }

            SearchOption(applied = sortApplied.value, text = "Сортировка", iconId = R.drawable.sort, showDialogState = showSort)
        }

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
            isInCartCheck = cartViewModel::isInCart
        )


        if (showFilter.value)
            FilterDialog(
                showDialog = showFilter,
                minInitValue = searchViewModel.minValue.toString(),
                maxInitValue = searchViewModel.maxValue.toString(),
                isOnHandOnlyInitValue = searchViewModel.isOnHandOnly,
                isMaleOnlyInitValue = searchViewModel.isMaleOnly,
                isFemaleOnlyInitValue = searchViewModel.isFemaleOnly,
                onApplyButtonClick = { moreThan, lessThan, isOnHand, isMaleOnly, isFemaleOnly ->

                    showFilter.value = false

                    try {
                        val minValue = moreThan.trim().replace(",", ".").toFloat()
                        val maxValue = lessThan.trim().replace(",", ".").toFloat()

                        searchViewModel.searchQuery(
                            query = query,
                            queryType = queryType,
                            minValue = minValue,
                            maxValue = maxValue,
                            isOnHandOnly = isOnHand,
                            isMaleOnly = isMaleOnly,
                            isFemaleOnly = isFemaleOnly
                        )
                    } catch (e: Exception) {
                        showToast(context = context, "Ошибка\nНекорректные данные")
                    }

                    filterApplied.value = true
                },
                onClearClick = {
                    searchViewModel.minValue = 0.0f
                    searchViewModel.maxValue = maxProductPrice
                    searchViewModel.isOnHandOnly = false
                    searchViewModel.isMaleOnly = false
                    searchViewModel.isFemaleOnly = false

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
    onAddToFavouriteClick: (ProductWithAmount) -> Unit,
    onAddToCartClick: (ProductWithAmount) -> Unit,
    onRemoveFromFavouriteClick: (String) -> Unit,
    onRemoveFromCartClick: (String) -> Unit,
    isInFavouriteCheck: (String) -> Boolean,
    isInCartCheck: (String) -> Boolean,
) {

    if (showProductList.value) {
        if (searchViewModel.isSuccess) {
            LazyProductList(
                onProductClick = onProductClick,
                listOfProductsWithAmounts = searchViewModel.searchProducts,

                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,

                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,

                isInFavouriteCheck = isInFavouriteCheck,
                isInCartCheck = isInCartCheck,

                onAmountChanged = searchViewModel::updateProductAmount,
                onCashStateChanged = searchViewModel::updateProductCashState,

                UploadMoreButton = { UploadMoreButton(searchViewModel = searchViewModel) }
            )
        }

        if (searchViewModel.isFailure)
            ErrorOccurred()
        else if (searchViewModel.isLoading)
            LoadingIndicator()
        else if (!searchViewModel.isLoading && searchViewModel.searchProducts.isEmpty())
            NothingFound()
    }
}

