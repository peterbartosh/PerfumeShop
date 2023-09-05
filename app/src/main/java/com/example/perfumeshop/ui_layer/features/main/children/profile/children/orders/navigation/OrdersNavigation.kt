package com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.ui.OrdersScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.ui.OrdersViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.ui.ProductProfileViewModel


const val ordersRoute = "profile orders"

fun NavController.navigateToOrders(navOptions: NavOptions? = null) {
    this.navigate(ordersRoute, navOptions)
}

fun NavGraphBuilder.ordersScreen(onProductClick : () -> Unit) {
    composable(route = ordersRoute) {
        val viewModel = hiltViewModel<OrdersViewModel>()
        val productProfileViewModel = hiltViewModel<ProductProfileViewModel>()

        OrdersScreen(viewModel = viewModel, onProductClick = { productId ->
            productProfileViewModel.updateProductId(productId)
            onProductClick.invoke()
        })
    }
}