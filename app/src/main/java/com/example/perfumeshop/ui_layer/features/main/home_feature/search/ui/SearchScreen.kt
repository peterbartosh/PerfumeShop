package com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui

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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.ProductWithAmount
import com.example.perfumeshop.data_layer.utils.QueryType
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.components.showToast
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

    //-------------------------------------------

    // Product list states

    val context = LocalContext.current

    val showProductList = remember {
        mutableStateOf(true)
    }

    val showPriceType = remember {
        mutableStateOf(false)
    }

    val selectedPriceType = remember {
        mutableStateOf(false)
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

    val moreThanState = remember {
        mutableStateOf("0")
    }
    val lessThanState = remember {
        mutableStateOf("0")
    }

    val isOnHandSelected = remember {
        mutableStateOf(false)
    }

    val volumesStates = remember {
        mutableStateListOf(false, false, false)
    }

    val volumes = listOf("35", "50", "125")

    val applyFilter = remember(moreThanState.value, lessThanState.value,
                               isOnHandSelected.value, volumesStates[0], volumesStates[1], volumesStates[2]) {
            mutableStateOf(
                moreThanState.value != "0" ||
                        lessThanState.value != "0" ||
                        isOnHandSelected.value ||
                        volumesStates.any { it }
        )
    }

    //-------------------------------------------

    // Sorting states

    val showSort = remember {
        mutableStateOf(false)
    }

    val sortStates = remember {
        mutableStateListOf(false, false)
    }

    val priorities = remember {
        mutableStateListOf<Int>()
    }

    val isAscending = remember {
        mutableStateOf(true)
    }

    //-------------------------------------------

    // comps

    Column(modifier = Modifier) {

        SearchForm() { q ->
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

            SearchOption(text = "Фильтр", iconId = R.drawable.filter, showState = showFilter)

            Box {
                SearchOption(text = "Цена", iconId = R.drawable.filter, showState = showPriceType)

                PriceDropDownMenu(
                    expanded = showPriceType,
                    selected = selectedPriceType
                )
            }

            SearchOption(text = "Сортировка", iconId = R.drawable.filter, showState = showSort)
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
            isCashPriceState = selectedPriceType,
            isInCartCheck = cartViewModel::isInCart
        )


        FilterDialog(
            showDialog = showFilter,
            moreThanState = moreThanState,
            lessThanState = lessThanState,
            isOnHandSelected = isOnHandSelected,
            states = volumesStates,
            volumes = volumes
        ) {
            showProductList.value = true
            showFilter.value = false

            try {
                val minValue = moreThanState.value.toFloat()
                val maxValue = lessThanState.value.toFloat()

                searchViewModel.searchQuery(
                    query = query,
                    queryType = queryType,
                    applyFilter = applyFilter.value,
                    minValue = minValue,
                    maxValue = maxValue,
                    volumes = volumes.filterIndexed{ ind, _ -> volumesStates[ind] }
                )
            } catch (e: Exception) {
                showToast(context = context, "Ошибка\nНекорректные данные")
            }
        }

        SortDialog(
            showDialog = showSort, states = sortStates,
            priorities = priorities, isAscending = isAscending
        ){
            showProductList.value = true
            showSort.value = false
            searchViewModel.sortProducts(priorities)
        }
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
                onProductClick = onProductClick,
                listOfProductsWithAmounts = searchViewModel.searchProducts,
                onAddToFavouriteClick = onAddToFavouriteClick,
                onAddToCartClick = onAddToCartClick,
                onRemoveFromFavouriteClick = onRemoveFromFavouriteClick,
                onRemoveFromCartClick = onRemoveFromCartClick,
                isInFavouriteCheck = isInFavouriteCheck,
                priceTypeState = isCashPriceState,
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

