package com.example.perfumeshop.ui_layer.features.main.children.home.children.product_home.ui

import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.example.perfumeshop.ui_layer.features.main.children.cart.children.product_cart.ui.ProductCartViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProductHomeViewModel @Inject constructor(private val repository: FireRepository) : ProductCartViewModel(repository) {

}



