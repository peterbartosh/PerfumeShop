package com.example.perfumeshop.presentation.features.cart

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.components.BackPressHandler
import com.example.perfumeshop.presentation.app.navigation.nested.cartActiveChild

const val cartRoute = "cart"

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    cartActiveChild = cartRoute
    this.navigate(cartRoute, navOptions)
}

fun NavGraphBuilder.cartScreen(
    //onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = cartRoute) {
        val cartViewModel = hiltViewModel<CartViewModel>()
        BackPressHandler(onBackPressed = onBackPressed)
        CartScreen(
            cartViewModel = cartViewModel,
            onOrderMakeClick = onOrderMakeClick,
            //onProductClick = onProductClick,
        )
    }
}