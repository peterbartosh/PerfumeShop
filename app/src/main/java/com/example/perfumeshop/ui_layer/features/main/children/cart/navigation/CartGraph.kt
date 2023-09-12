package com.example.perfumeshop.ui_layer.features.main.children.cart.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartScreen
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.product.navigation.productScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel


//const val cartGraphRoute = "cartGraph"

var cartActiveChild by mutableStateOf(cartRoute)

//fun NavController.navigateToCartGraph(){
//    this.navigateToGraph(graphRoute = cartGraphRoute, saveStartDest = true){}
//}


fun NavGraphBuilder.cartGraph(
        onProductClick: (String) -> Unit,
        cartViewModel: CartViewModel,
        favouriteViewModel: FavouriteViewModel
){
        cartScreen(
                cartViewModel = cartViewModel,
                favouriteViewModel = favouriteViewModel,
                onProductClick = onProductClick)

        productScreen(
                cartViewModel = cartViewModel,
                favouriteViewModel = favouriteViewModel,
                onClick = {}
        )
}