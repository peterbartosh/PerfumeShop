package com.example.perfumeshop.ui_layer.features.main.cart_feature.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartScreen
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel


const val cartRoute = "cart"
var cartActiveChild by mutableStateOf(cartRoute)

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    this.navigate(cartRoute, navOptions)
}

fun NavGraphBuilder.cartScreen(
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {
    composable(route = cartRoute) {
        cartViewModel.isFailure = false
        cartViewModel.isLoading = false
        cartViewModel.isSuccess = false
        CartScreen(
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            onProductClick = onProductClick
        )
    }
}