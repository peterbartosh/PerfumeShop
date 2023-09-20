package com.example.perfumeshop.ui_layer.features.main.cart_feature.cart.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.components.SubmitButton
import com.example.perfumeshop.ui_layer.components.showToast
import com.example.perfumeshop.ui_layer.features.main.home_feature.search.ui.LazyProductList
import com.example.perfumeshop.ui_layer.features.main.profile_feature.favourite.ui.FavouriteViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(
    favouriteViewModel: FavouriteViewModel,
    cartViewModel: CartViewModel,
    onProductClick: (String) -> Unit,
    onOrderMakeClick: () -> Unit
) {



//                if (cartViewModel.initLoadingProducts) {
//                        cartViewModel.loadUserProducts()
//                }

                val context = LocalContext.current

                Column(
                    modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {

//                        Text(text = "YOUR CART")
//
//                        Spacer(modifier = Modifier.height(100.dp))

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

                    SubmitButton(text = "Перейти к оформлению заказа") {
                        if (FirebaseAuth.getInstance().currentUser?.isAnonymous == false)
                            onOrderMakeClick()
                        else
                            showToast(context, "Вы не авторизованы")
                    }
        }
}


@Composable
fun CartIsEmpty() {
        Text(text = "CART IS EMPTY")
}