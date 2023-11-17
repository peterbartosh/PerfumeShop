package com.example.perfumeshop.presentation.features.main

import androidx.navigation.NavGraphBuilder
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.QueryType
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.navigation.cartRoute
import com.example.perfumeshop.presentation.features.main.cart_feature.cartActiveChild
import com.example.perfumeshop.presentation.features.main.cart_feature.cartGraph
import com.example.perfumeshop.presentation.features.main.home_feature.home.navigation.homeRoute
import com.example.perfumeshop.presentation.features.main.home_feature.homeActiveChild
import com.example.perfumeshop.presentation.features.main.home_feature.homeGraph
import com.example.perfumeshop.presentation.features.main.profile_feature.profile.navigation.profileRoute
import com.example.perfumeshop.presentation.features.main.profile_feature.profileActiveChild
import com.example.perfumeshop.presentation.features.main.profile_feature.profileGraph
import com.example.perfumeshop.presentation.features.main.settings_feature.navigation.settingsScreen


fun getActiveChild(parentRoute : String) : String{
    return when(parentRoute){
        homeRoute -> homeActiveChild
        cartRoute -> cartActiveChild
        profileRoute -> profileActiveChild
        else -> parentRoute
    }
}

fun NavGraphBuilder.mainGraph(
    onUserPreferencesChanged: (UserPreferencesType, Int) -> Unit,
    navigateSearch: (String, QueryType) -> Unit,
    navigateOption: (OptionType) -> Unit,
    //navigateProduct: (String) -> Unit,
    navigateToOrderMaking: () -> Unit,
    navigateToCart: () -> Unit,
    //homeViewModel: HomeViewModel,
    onBackPressed: () -> Unit
){

        homeGraph(
            onSearchClick = navigateSearch,
            //onProductClick = navigateProduct,
            onBackPressed = onBackPressed
        )

        cartGraph(
            //onProductClick = navigateProduct,
            onOrderDone = navigateToCart,
            onOrderMakeClick = navigateToOrderMaking,
            onBackPressed = onBackPressed
        )

        profileGraph(
            onOptionClick = navigateOption,
            //onProductClick = navigateProduct,
            onBackPressed = onBackPressed
        )

//        productScreen(
//            onClick = {},
//            cartViewModel = cartViewModel,
//            favouriteViewModel = favouriteViewModel,
//            onBackPressed = onBackPressed
//        )

        settingsScreen(
            onUserPreferencesChanged = onUserPreferencesChanged,
            onBackPressed = onBackPressed
        )


}