package com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel


@Composable
fun ProductProfileScreen(productId : String, viewModel: ViewModel, onClick : () -> Unit) {

    Text(text = productId)

//    when (viewModel) {
//        is ProductHomeViewModel -> Text(text = )
//        is ProductSearchViewModel -> Text(text = SearchSelectedProductState.productId)
//        is ProductCartViewModel -> Text(text = CartSelectedProductState.productId)
//    }


}