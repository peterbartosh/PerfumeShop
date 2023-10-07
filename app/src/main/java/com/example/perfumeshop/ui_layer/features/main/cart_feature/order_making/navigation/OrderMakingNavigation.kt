package com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.ui.OrderMakingScreen
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.ui.OrderMakingViewModel
import kotlin.random.Random


const val orderMakingRoute = "order_making"

fun NavController.navigateToOrderMaking(navOptions: NavOptions? = null) {
    cartActiveChild = orderMakingRoute
    this.navigate(route = orderMakingRoute, navOptions)
}

fun NavController.navigateToOrderMakingWithoutSavingState(navOptions: NavOptions? = null) {
    cartActiveChild = orderMakingRoute
    this.navigate(route = orderMakingRoute, navOptions)
}

fun NavGraphBuilder.orderMakingScreen(
    cartViewModel: CartViewModel,
    onOrderDone: () -> Unit) {
    composable(route = orderMakingRoute) {
        val orderMakingViewModel = hiltViewModel<OrderMakingViewModel>()
        val seed = Random(243).nextInt()
        OrderMakingScreen(
            cartViewModel = cartViewModel,
            orderMakingViewModel = orderMakingViewModel,
            onOrderDone = onOrderDone,
            seed = seed
        )
    }
}