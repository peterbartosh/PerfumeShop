package com.example.perfumeshop.ui_layer.features.main.children.product.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel


@Composable
fun ProductScreen(
    productId: String,
    productViewModel: ViewModel,
    onClick: () -> Unit,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel
) {

    Text(text = productId)

//    when (viewModel) {
//        is ProductHomeViewModel -> Text(text = )
//        is ProductSearchViewModel -> Text(text = SearchSelectedProductState.productId)
//        is ProductCartViewModel -> Text(text = CartSelectedProductState.productId)
//    }


}