package com.example.perfumeshop.ui_layer.features.main.children.profile.children.product_profile.ui

import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.cart.ui.CartViewModel
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.ui.ProductCartViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject




@HiltViewModel
class ProductProfileViewModel @Inject constructor(private val repository: FireRepository) : ProductCartViewModel(repository) {

}


