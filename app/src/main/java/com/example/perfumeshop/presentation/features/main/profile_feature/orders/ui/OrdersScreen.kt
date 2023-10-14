package com.example.perfumeshop.presentation.features.main.profile_feature.orders.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.perfumeshop.presentation.components.LoadingIndicator
import com.example.perfumeshop.presentation.features.main.cart_feature.cart.ui.CartViewModel
import com.example.perfumeshop.presentation.features.main.profile_feature.favourite.ui.FavouriteViewModel

@Composable
fun OrdersScreen(
    ordersViewModel: OrdersViewModel,
    cartViewModel: CartViewModel,
    favouriteViewModel: FavouriteViewModel,
    onProductClick : (String) -> Unit
) {

    val str = "str"

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
