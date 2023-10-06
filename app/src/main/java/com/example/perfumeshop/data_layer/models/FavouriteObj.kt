package com.example.perfumeshop.data_layer.models

import com.google.firebase.firestore.PropertyName

data class FavouriteObj(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId : String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId : String? = null
)