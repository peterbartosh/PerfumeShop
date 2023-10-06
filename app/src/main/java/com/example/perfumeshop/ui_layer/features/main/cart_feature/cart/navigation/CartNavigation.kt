package com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartScreen
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel

const val cartRoute = "cart"

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    this.navigate(cartRoute, navOptions)
}

fun NavGraphBuilder.cartScreen(
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onOrderMakeClick: () -> Unit
) {
    composable(route = cartRoute) {
        CartScreen(
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            onProductClick = onProductClick,
            onOrderMakeClick = onOrderMakeClick
        )
    }
}