package com.example.perfumeshop.data.models

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
    @Exclude var id: String? = null,

    var number: String? = null,

    var address: String? = null,

    var date: Timestamp?  = null,

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
)