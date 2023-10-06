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
        OrderMakingScreen(cartViewModel = cartViewModel,
                          orderMakingViewModel = orderMakingViewModel,
                          onOrderDone = onOrderDone)
    }
}