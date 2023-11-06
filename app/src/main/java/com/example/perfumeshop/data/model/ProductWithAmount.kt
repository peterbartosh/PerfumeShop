package com.example.perfumeshop.data.model


data class ProductWithAmount(
    var product: Product? = null,
    var amountCash : Int? = 0,
    var amountCashless : Int? = 0
)