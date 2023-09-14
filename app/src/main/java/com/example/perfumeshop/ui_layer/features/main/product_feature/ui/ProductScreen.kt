package com.example.perfumeshop.ui_layer.features.main.product_feature.ui

import androidx.compose.runtime.Composable
import com.example.perfumeshop.ui_layer.features.main.cart_feature.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel


@Composable
fun ProductScreen(
    productId: String,
    productViewModel: ProductViewModel,
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    if (!productViewModel.isInitialized){
        productViewModel.loadProduct(productId)
    }

}

@Composable
fun ShowContent() {

}