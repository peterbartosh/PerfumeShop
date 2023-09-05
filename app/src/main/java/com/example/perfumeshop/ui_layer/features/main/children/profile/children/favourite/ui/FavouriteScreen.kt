package com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FavouriteScreen(viewModel: FavouriteViewModel, onProductClick : (String) -> Unit) {
    Text(text = "FAV")
}