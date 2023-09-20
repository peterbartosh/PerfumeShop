package com.example.perfumeshop.ui_layer.features.main.cart_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.navigation.cartScreen
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.cart_feature.order_making.navigation.orderMakingScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel


var cartActiveChild by mutableStateOf(cartRoute)


fun NavGraphBuilder.cartGraph(
    onOrderDone : () -> Unit,
    onOrderMakeClick : () -> Unit,
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    cartScreen(cartViewModel = cartViewModel,
               favouriteViewModel = favouriteViewModel,
               onProductClick = onProductClick,
               onOrderMakeClick = onOrderMakeClick)

    orderMakingScreen(cartViewModel = cartViewModel, onOrderDone = onOrderDone)


}