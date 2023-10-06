package com.example.perfumeshop.data_layer.models

import com.google.firebase.firestore.PropertyName

data class CartObj(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId : String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId : String? = null,

    @get:PropertyName("is_cash_price")
    @set:PropertyName("is_cash_price")
    var isCashPrice : Boolean? = null,

    var amount : Int? = null,
)