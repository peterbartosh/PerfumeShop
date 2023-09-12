package com.example.perfumeshop.ui_layer.features.main.children.profile.children.orders.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import io.grpc.Status

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel, onProductClick : (String) -> Unit) {

    val listOfOrders = ordersViewModel.ordersList.collectAsState().value

    LazyColumn{
        items(listOfOrders){ order ->
            OrderRow(order = order, onProductClick = onProductClick)
        }
    }

}
@Composable
fun OrderRow(order : Order, onProductClick : (String) -> Unit) {
    Text(text = order.productId ?: "id not found")
}