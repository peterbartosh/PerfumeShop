package com.example.perfumeshop.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import java.sql.Timestamp

data class Review(
    @Exclude var id : String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId : String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId : String? = null,

    var content : String? = null,

    var authorName : String? = null,

    var rating : Int? = null,

    var date : Timestamp?  = null

)
