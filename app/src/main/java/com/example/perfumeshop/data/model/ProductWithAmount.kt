package com.example.perfumeshop.data.model


data class ProductWithAmount(
    var product: Product? = null,
    var cashPriceAmount : Int? = 0,
    var cashlessPriceAmount : Int? = 0
)