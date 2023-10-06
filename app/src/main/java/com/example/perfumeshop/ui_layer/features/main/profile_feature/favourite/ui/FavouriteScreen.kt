package com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.perfumeshop.R
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartIsEmpty
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.LazyProductList

@Composable
fun FavouriteScreen(
    favouriteViewModel: FavouriteViewModel,
    onProductClick: (String) -> Unit
) {

    val products by favouriteViewModel.userProducts.collectAsState()

    if (favouriteViewModel.isSuccess)
        LazyProductList(
            listOfProducts = products,
            onProductClick = onProductClick,
            onAddToFavouriteClick = favouriteViewModel::addToFavourite,
            onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
            isInFavouriteCheck = favouriteViewModel::isInFavourite
        )


    else if (favouriteViewModel.isFailure)
        Text(text = stringResource(id = R.string.error_occured))
    else if (favouriteViewModel.isLoading)
        LoadingIndicator()
    else if (!favouriteViewModel.isLoading && products.isEmpty())
        CartIsEmpty()
}