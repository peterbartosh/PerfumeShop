package com.example.perfumeshop.data_layer.models

import java.io.Serializable


data class ProductWithAmount(
    var product: Product? = null,
    var amount : Int? = 1,
    var isCashPrice : Boolean? = null
)