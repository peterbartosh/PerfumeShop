package com.example.perfumeshop.data_layer.models

import com.example.perfumeshop.data_layer.utils.Sex
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Review(
    @Exclude var id : String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId : String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId : String? = null,

    var content : String? = null,

    var rating : String? = null,

    var date : Timestamp?  = null

)
