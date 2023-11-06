package com.example.perfumeshop.data.model

import com.google.firebase.firestore.PropertyName

data class CartObj(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId : String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId : String? = null,

    @get:PropertyName("cash_price_amount")
    @set:PropertyName("cash_price_amount")
    var cashPriceAmount : Int? = null,

    @get:PropertyName("cashless_price_amount")
    @set:PropertyName("cashless_price_amount")
    var cashlessPriceAmount : Int? = null,
)