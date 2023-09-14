package com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.LazyProductList
import com.example.perfumeshop.ui_layer.features.main.children.profile.children.favourite.ui.FavouriteViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(favouriteViewModel: FavouriteViewModel,
               cartViewModel: CartViewModel,
               onProductClick : (String) -> Unit) {


        Surface(modifier = Modifier) {

//                if (cartViewModel.initLoadingProducts) {
//                        cartViewModel.loadUserProducts()
//                }


                Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {

                        Text(text = "YOUR CART")

                        Spacer(modifier = Modifier.height(100.dp))


                        if (cartViewModel.isInitialized)
                                if (cartViewModel.isFailure) {
                                        Text(text = "ERROR")
                                } else if (cartViewModel.isLoading)
                                        LoadingIndicator()
                                  else if (cartViewModel.userProducts.collectAsState().value.isEmpty())
                                        CartIsEmpty()
                                  else
                                        LazyProductList(
                                            onProductClick = onProductClick,
                                            listOfProducts = cartViewModel.userProducts.collectAsState().value,
                                            onAddToFavouriteClick = favouriteViewModel::addToFavourite,
                                            onAddToCartClick = cartViewModel::addToCart,
                                            onRemoveFromFavouriteClick = favouriteViewModel::removeFromFavourite,
                                            onRemoveFromCartClick = cartViewModel::removeFromCart,
                                            isInCartCheck = cartViewModel::isInCart,
                                            isInFavouriteCheck = favouriteViewModel::isInFavourite
                                        )
                }


        }
}


@Composable
fun CartIsEmpty() {
        Text(text = "CART IS EMPTY")
}