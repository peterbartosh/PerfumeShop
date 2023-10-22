package com.example.perfumeshop.data.model


data class ProductWithAmount(
    var product: Product? = null,
    var amount : Int? = 1,
    var isCashPrice : Boolean? = null
)