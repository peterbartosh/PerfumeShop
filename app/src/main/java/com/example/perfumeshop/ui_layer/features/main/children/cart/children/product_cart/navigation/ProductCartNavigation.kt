package com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.ui.ProductCartScreen
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.ui.ProductCartViewModel

const val cartProductRoute = "cart product"


fun NavController.navigateToProductCart(navOptions: NavOptions? = null) {
    this.navigate(cartProductRoute, navOptions)
}


fun NavGraphBuilder.productCartScreen(onClick : () -> Unit) {
    composable(route = cartProductRoute) {
        val viewModel = hiltViewModel<ProductCartViewModel>()
        ProductCartScreen(productId = "productId", viewModel = viewModel, onClick = onClick)
    }

}
