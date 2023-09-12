package com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.ui.OrdersScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.ui.OrdersViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.navigation.profileActiveChild


const val ordersRoute = "orders"

fun NavController.navigateToOrders(navOptions: NavOptions? = null) {
    profileActiveChild = ordersRoute
    this.navigate(ordersRoute, navOptions)
}

fun NavGraphBuilder.ordersScreen(onProductClick : (String) -> Unit) {
    composable(route = ordersRoute) {
        val viewModel = hiltViewModel<OrdersViewModel>()
        OrdersScreen(ordersViewModel = viewModel, onProductClick = onProductClick)
    }
}