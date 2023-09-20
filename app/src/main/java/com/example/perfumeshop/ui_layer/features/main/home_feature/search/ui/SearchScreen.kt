package com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.theme.Gold
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel


@Composable
fun SearchScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    searchViewModel: SearchViewModel,
    onProductClick: (String) -> Unit,
    query: String,
    queryType: QueryType,
) {

    Log.d("QUERY_TEST", "SearchScreen: ${query + " " + queryType.name}")

    val showProductList = remember {
        mutableStateOf(true)
    }

    val showFilter = remember {
        mutableStateOf(false)
    }

    var updateQuery by rememberSaveable(inputs = arrayOf(query, queryType)) {
        mutableStateOf(true)
    }

    if (updateQuery) {
        searchViewModel.searchQuery(query = query, queryType = queryType)
        updateQuery = false
    }

    Column(modifier = Modifier) {

        SearchForm() { q ->
            searchViewModel.searchQuery(query = q, queryType = QueryType.brand)
            //showProductList.value = true
           // updateQuery = false
        }
        
        Row(horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)) {
            Icon(painter = painterResource(id = R.drawable.filter),
                 tint = Gold,
                 contentDescription = "filter icon",
                 modifier = Modifier.clickable { showFilter.value = !showFilter.value })
        }

        ShowProductList(
            showProductList = showProductList,
            viewModel = searchViewModel,
            onProductClick = onProductClick,
            onAddToCartClick = cartViewModel::addToCart,
            onAddToFavouriteClick = favouriteViewModel::addToFavourite,
            onRemoveFromCartClick = cartViewModel::removeFromCart,
            onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
            isInFavouriteCheck = favouriteViewModel::isInFavourite,
            isInCartCheck = cartViewModel::isInCart
        )

        val rangeSliderState = remember {
            mutableStateOf(0f..10f)
        }

        val selected = remember {
            mutableStateOf(false)
        }

        val listOfButtonsStates = remember {
            listOf(Pair(mutableStateOf(false), 35),
                                  Pair(mutableStateOf(false), 50),
                                  Pair(mutableStateOf(false), 125))
        }

        val applyFilter = remember(rangeSliderState, selected, listOfButtonsStates) {
            mutableStateOf(
                rangeSliderState.value != 0f..10f ||
                        selected.value ||
                        listOfButtonsStates.any { it.first.value }
            )
        }

            FilterList(showDialog = showFilter,
                       rangeSliderState = rangeSliderState,
                       selected = selected,
                       listOfButtonsStates = listOfButtonsStates,
            onApplyButtonClick = {
                showProductList.value = true
                showFilter.value = false

                searchViewModel.searchQuery(
                    query = query,
                    queryType = queryType,
                    applyFilter = applyFilter.value,
                    minValue = rangeSliderState.value.start,
                    maxValue = rangeSliderState.value.endInclusive,
                    volume = listOfButtonsStates.filter { it.first.value }.map { it.second }
                )
            })
    }
}

@Composable
fun ShowProductList(
    showProductList: MutableState<Boolean>,
    viewModel: SearchViewModel,
    onProductClick: (String) -> Unit,
    onAddToFavouriteClick : (Product) -> Unit,
    onAddToCartClick : (Product) -> Unit,
    onRemoveFromFavouriteClick : (String) -> Unit,
    onRemoveFromCartClick : (String) -> Unit,
    isInFavouriteCheck : (String) -> Boolean,
    isInCartCheck : (String) -> Boolean
) {

    if (showProductList.value)
    if (viewModel.isSuccess)
        LazyProductList(
            onProductClick = onProductClick,
            listOfProducts = viewModel.searchList.collectAsState().value,
            onAddToFavouriteClick = onAddToFavouriteClick,
            onAddToCartClick = onAddToCartClick,
            onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
            onRemoveFromCartClick = onRemoveFromCartClick,
            isInFavouriteCheck = isInFavouriteCheck,
            isInCartCheck = isInCartCheck
        )
    if (viewModel.isFailure)
        Text(text = "ERROR")
    else if (viewModel.isLoading)
        LoadingIndicator()
    else if (!viewModel.isLoading && viewModel.searchList.collectAsState().value.isEmpty())
        viewModel.isFailure = true
}

