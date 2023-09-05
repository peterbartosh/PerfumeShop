package com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun OrdersScreen(viewModel: OrdersViewModel, onProductClick : (String) -> Unit) {
    Text(text = "ORD")
}