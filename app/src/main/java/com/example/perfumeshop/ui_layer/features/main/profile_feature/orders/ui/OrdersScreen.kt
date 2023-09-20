package com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.LazyProductList

@Composable
fun OrdersScreen(ordersViewModel: OrdersViewModel, onProductClick : (String) -> Unit) {



    val listOfOrders = ordersViewModel.ordersList.collectAsState()

    if (ordersViewModel.isSuccess)
        LazyColumn{
            items(listOfOrders.value){ order ->
                OrderRow(order = order, onProductClick = onProductClick)
            }
        }
    if (ordersViewModel.isFailure)
        Text(text = "ERROR")
    else if (ordersViewModel.isLoading)
        LoadingIndicator()
    else if (!ordersViewModel.isLoading && listOfOrders.value.isEmpty())
        ordersViewModel.isFailure = true


}
