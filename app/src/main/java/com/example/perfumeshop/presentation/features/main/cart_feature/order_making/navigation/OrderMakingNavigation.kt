package com.example.perfumeshop.presentation.features.main.cart_feature.order_making.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.presentation.app.navigation.BackPressHandler
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.ui.OrderMakingScreen
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.ui.OrderMakingViewModel


const val orderMakingRoute = "order_making"

fun NavController.navigateToOrderMaking(navOptions: NavOptions? = null) {
    cartActiveChild = orderMakingRoute
    this.navigate(route = orderMakingRoute, navOptions)
}

fun NavGraphBuilder.orderMakingScreen(
    onOrderDone: () -> Unit,
    onBackPressed: () -> Unit
) {
    composable(route = orderMakingRoute) {
        val orderMakingViewModel = hiltViewModel<OrderMakingViewModel>()

        BackPressHandler(onBackPressed = onBackPressed)

        OrderMakingScreen(
            orderMakingViewModel = orderMakingViewModel,
            onOrderDone = onOrderDone
        )
    }
}