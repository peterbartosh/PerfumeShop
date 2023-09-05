package com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartScreen
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.ui.ProductCartViewModel


const val cartRoute = "cart"

fun NavController.navigateToCart(navOptions: NavOptions? = null) {
    this.navigate(cartRoute, navOptions)
}

fun NavGraphBuilder.cartScreen(onProductClick : () -> Unit) {
    composable(route = cartRoute) {
        val viewModel = hiltViewModel<CartViewModel>()
        val productCartViewModel = hiltViewModel<ProductCartViewModel>()
        CartScreen(viewModel = viewModel, onProductClick = { productId ->
            productCartViewModel.updateProductId(productId)
            onProductClick.invoke()
        })
    }
}