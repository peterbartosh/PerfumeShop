package com.example.perfumeshop.data_layer.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

sealed class Status(){
    class Canceled() : Status()
    class Processing() : Status()
    class Accepted() : Status()
    class InProgress() : Status()
    class Delivering() : Status()
    class Success() : Status()
}

data class Order(
    @Exclude var id : String? = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId : String? = null,

    @get:PropertyName("product_id")
    @set:PropertyName("product_id")
    var productId : String? = null,

    var amount : Int? = null,

    var date : Timestamp?  = null,

    var status : Status
)
