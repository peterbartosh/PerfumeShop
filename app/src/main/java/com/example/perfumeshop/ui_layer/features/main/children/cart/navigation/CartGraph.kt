package com.example.perfumeshop.ui_layer.features.main.children.cart.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.perfumeshop.data_layer.utils.cartGraphR
import com.example.perfumeshop.data_layer.utils.navigateToGraph
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartRoute
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.navigation.cartScreen
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.navigation.productCartScreen


const val cartGraphRoute = "cartGraph"

var cartGraphStartDestination = mutableStateOf(cartRoute)

//fun NavController.navigateToCartGraph(){
//    this.navigateToGraph(graphRoute = cartGraphRoute, saveStartDest = true){}
//}


fun NavGraphBuilder.cartGraph(onProductClick: () -> Unit){

    //navigation(startDestination = cartGraphStartDestination.value, route = cartGraphRoute){


        cartScreen(onProductClick = onProductClick)

        productCartScreen(onClick = {})


   // }
}