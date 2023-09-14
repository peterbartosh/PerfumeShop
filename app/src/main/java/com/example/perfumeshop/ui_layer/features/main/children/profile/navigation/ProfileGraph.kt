package com.example.perfumeshop.ui_layer.features.main.children.profile.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.edit_profile.navigation.editProfileScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.navigation.favouriteScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.navigation.ordersScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.navigation.profileScreen
import com.example.perfumeshop.ui_layer.features.main.children.product.navigation.productScreen
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel


//const val profileGraphRoute = "profileGraph"

var profileActiveChild by mutableStateOf(profileRoute)

fun NavGraphBuilder.profileGraph(
    onOptionClick: (OptionType) -> Unit,
    onProductClick: (String) -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
){
        profileScreen(onOptionClick = onOptionClick)

        editProfileScreen(onClick = {})

        favouriteScreen(onProductClick = onProductClick,
                        favouriteViewModel = favouriteViewModel,
                        cartViewModel = cartViewModel)

        ordersScreen(onProductClick = onProductClick)

        productScreen(
            onClick = {},
            cartViewModel = cartViewModel,
            favouriteViewModel = favouriteViewModel
        )
}