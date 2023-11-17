package com.example.perfumeshop.presentation.features.main.cart_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartScreen
import com.example.perfumeshop.presentation.features.main.cart_feature.order_making.navigation.orderMakingScreen


var cartActiveChild by mutableStateOf(cartRoute)


fun NavGraphBuilder.cartGraph(
    onOrderDone: () -> Unit,
    onOrderMakeClick: () -> Unit,
    //onProductClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    cartScreen(
        //onProductClick = onProductClick,
        onOrderMakeClick = onOrderMakeClick,
        onBackPressed = onBackPressed
    )

    orderMakingScreen(
        onOrderDone = onOrderDone,
        onBackPressed = onBackPressed
    )


}