package com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.LazyProductList

@Composable
fun FavouriteScreen(
    favouriteViewModel: FavouriteViewModel,
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel
) {

    favouriteViewModel.isSuccess = true

    val listOfProducts = favouriteViewModel.userProducts.collectAsState().value

    if (favouriteViewModel.isSuccess)
        LazyProductList(
            userScrollEnabled = true,
            listOfProducts = listOfProducts,
            onProductClick = onProductClick,
            onAddToFavouriteClick = favouriteViewModel::addToFavourite,
            onAddToCartClick = cartViewModel::addToCart,
            onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
            onRemoveFromCartClick = cartViewModel::removeFromCart,
            isInFavouriteCheck = favouriteViewModel::isInFavourite,
            isInCartCheck = cartViewModel::isInCart
    )
    else if (favouriteViewModel.isLoading) LoadingIndicator()
    else if (favouriteViewModel.isFailure) Text(text = "ERROR")
}