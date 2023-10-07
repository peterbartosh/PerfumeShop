package com.example.perfumeshop.ui_layer.features.main.profile_feature.orders.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.perfumeshop.data_layer.models.Order
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.LazyProductList
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel

@Composable
fun OrdersScreen(
    ordersViewModel: OrdersViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onProductClick : (String) -> Unit
) {

    if (ordersViewModel.isSuccess)
        LazyColumn{
            items(ordersViewModel.ordersList){ order ->
                OrderRow(
                    order = order,
                    productsWithAmount = ordersViewModel.productsWithAmountMap[order.id] ?: emptyList(),
                    onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                    onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                    isInFavouriteCheck = favouriteViewModel::isInFavourite,
                    onAddToCartClick = cartViewModel::addToCart,
                    onRemoveFromCartClick = cartViewModel::removeFromCart,
                    isInCartCheck = cartViewModel::isInCart,
                    onProductClick = onProductClick
                )
            }
        }
    if (ordersViewModel.isFailure)
        Text(text = "Произошла ошибка")
    else if (ordersViewModel.isLoading)
        LoadingIndicator()
    else if (!ordersViewModel.isLoading && ordersViewModel.ordersList.isEmpty())
        Text(text = "Ничего не найдено")


}
