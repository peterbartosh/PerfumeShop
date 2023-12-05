package com.example.perfumeshop.presentation.features.orders

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.components.BackPressHandler
import com.example.perfumeshop.presentation.app.navigation.nested.profileActiveChild


const val ordersRoute = "orders"

fun NavController.navigateToOrders(navOptions: NavOptions? = null) {
    profileActiveChild = ordersRoute
    this.navigate(ordersRoute, navOptions)
}

fun NavGraphBuilder.ordersScreen(
    //onProductClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = ordersRoute) {
        val ordersViewModel = hiltViewModel<OrdersViewModel>()

        BackPressHandler(onBackPressed = onBackPressed)
        OrdersScreen(
            ordersViewModel = ordersViewModel,
            //onProductClick = onProductClick
        )
    }
}