package com.example.perfumeshop.ui_layer.features.main.profile_feature

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data_layer.utils.OptionType
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.edit_profile.navigation.editProfileScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.navigation.favouriteScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.navigation.ordersScreen
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.ui_layer.features.main.profile_feature.profile.navigation.profileScreen


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

}