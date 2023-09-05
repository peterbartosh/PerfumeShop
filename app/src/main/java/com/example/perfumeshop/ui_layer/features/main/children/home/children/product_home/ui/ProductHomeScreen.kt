package com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel


@Composable
fun ProductHomeScreen(productId : String, viewModel: ViewModel) {

    Text(text = productId)

//    when (viewModel) {
//        is ProductHomeViewModel -> Text(text = )
//        is ProductSearchViewModel -> Text(text = SearchSelectedProductState.productId)
//        is ProductCartViewModel -> Text(text = CartSelectedProductState.productId)
//    }


}