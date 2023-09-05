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
import com.example.perfumeshop.ui_layer.features.main.children.home.children.search.ui.ProductList
import com.google.firebase.auth.FirebaseAuth

@Composable
fun CartScreen(viewModel: CartViewModel, onProductClick : (String) -> Unit) {


        Surface(modifier = Modifier) {

                if (viewModel.initLoadingProducts) {
                        viewModel.loadUserProducts(userId = FirebaseAuth.getInstance().currentUser?.uid.toString())
                }


                Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {

                        Text(text = "YOUR CART")

                        Spacer(modifier = Modifier.height(100.dp))


                        if (viewModel.isInitialized)
                                if (viewModel.isFailure) {
                                        Text(text = "ERROR")
                                } else if (viewModel.isLoading) {
                                        Column(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalAlignment = Alignment.Start,
                                                verticalArrangement = Arrangement.Center
                                        ) {
                                                Text(text = "Loading...")
                                                LinearProgressIndicator()
                                        }
                                } else if (viewModel.userProducts.collectAsState().value.isEmpty())
                                        CartIsEmpty()
                                  else
                                        ProductList(
                                                onProductClick = onProductClick,
                                                listOfProducts = viewModel.userProducts.collectAsState().value
                                        )
                }


        }
}


@Composable
fun CartIsEmpty() {
        Text(text = "CART IS EMPTY")
}