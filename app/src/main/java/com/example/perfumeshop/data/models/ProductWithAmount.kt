package com.example.perfumeshop.data.models


data class ProductWithAmount(
    var product: Product? = null,
    var amount : Int? = 1,
    var isCashPrice : Boolean? = null
)