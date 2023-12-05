package com.example.perfumeshop.presentation.app.navigation.nested

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.presentation.features.cart.cartRoute
import com.example.perfumeshop.presentation.features.cart.cartScreen
import com.example.perfumeshop.presentation.features.edit_profile.editProfileScreen
import com.example.perfumeshop.presentation.features.favourite.favouriteScreen
import com.example.perfumeshop.presentation.features.home.homeRoute
import com.example.perfumeshop.presentation.features.home.homeScreen
import com.example.perfumeshop.presentation.features.order_making.orderMakingScreen
import com.example.perfumeshop.presentation.features.orders.ordersScreen
import com.example.perfumeshop.presentation.features.profile.profileRoute
import com.example.perfumeshop.presentation.features.profile.profileScreen
import com.example.perfumeshop.presentation.features.search.searchScreen
import com.example.perfumeshop.presentation.features.settings.settingsScreen

var profileActiveChild = profileRoute
var homeActiveChild = homeRoute
var cartActiveChild = cartRoute

fun getActiveChild(parentRoute : String) : String{
    return when(parentRoute){
        homeRoute -> homeActiveChild
        cartRoute -> cartActiveChild
        profileRoute -> profileActiveChild
        else -> parentRoute
    }
}

fun NavGraphBuilder.mainGraph(
    navigateToSearch: (String, QueryType) -> Unit,
    navigateToOption: (OptionType) -> Unit,
    //navigateProduct: (String) -> Unit,
    navigateToOrderMaking: () -> Unit,
    navigateToCart: () -> Unit,
    //homeViewModel: HomeViewModel,
    onBackPressed: () -> Unit,
    onUserPreferencesChanged: (UserPreferencesType, Int) -> Unit
){
    // home
    homeScreen(
        onSearchClick = navigateToSearch,
        onBackPressed = onBackPressed
    )

    searchScreen(
        // onProductClick = onProductClick,
        onBackPressed = onBackPressed
    )

    // cart
    cartScreen(
        //onProductClick = onProductClick,
        onOrderMakeClick = navigateToOrderMaking,
        onBackPressed = onBackPressed
    )

    orderMakingScreen(
        onOrderDone = navigateToCart,
        onBackPressed = onBackPressed
    )

    // profile
    profileScreen(
        onOptionClick = navigateToOption,
        onBackPressed = onBackPressed
    )

    editProfileScreen(
        onClick = {},
        onBackPressed = onBackPressed
    )

    favouriteScreen(
        //onProductClick = onProductClick,
        onBackPressed = onBackPressed
    )

    ordersScreen(
        //onProductClick = onProductClick,
        onBackPressed = onBackPressed
    )

//        productScreen(
//            onClick = {},
//            cartViewModel = cartViewModel,
//            favouriteViewModel = favouriteViewModel,
//            onBackPressed = onBackPressed
//        )

    // settings
    settingsScreen(
        onUserPreferencesChanged = onUserPreferencesChanged,
        onBackPressed = onBackPressed
    )


}