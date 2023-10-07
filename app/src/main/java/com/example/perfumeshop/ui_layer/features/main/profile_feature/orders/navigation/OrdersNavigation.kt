package com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.ui.OrdersScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.ui.OrdersViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profileActiveChild


const val ordersRoute = "orders"

fun NavController.navigateToOrders(navOptions: NavOptions? = null) {
    profileActiveChild = ordersRoute
    this.navigate(ordersRoute, navOptions)
}

fun NavGraphBuilder.ordersScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    onProductClick : (String) -> Unit
) {
    composable(route = ordersRoute) {
        val ordersViewModel = hiltViewModel<OrdersViewModel>()
//        if (profileActiveChild != ordersRoute) {
//            ordersViewModel.isLoading = false
//            ordersViewModel.isFailure = false
//            ordersViewModel.isSuccess = false
//            //ordersViewModel.initOrdersQuery = true
//        }
        OrdersScreen(
            ordersViewModel = ordersViewModel,
            favouriteViewModel = favouriteViewModel,
            cartViewModel = cartViewModel,
            onProductClick = onProductClick
        )
    }
}