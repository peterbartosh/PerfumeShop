package com.example.perfumeshop.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class User(
    @Exclude
    var id: String? = null,

    @get:PropertyName("first_name")
    @set:PropertyName("first_name")
    var firstName: String? = null,

    @get:PropertyName("second_name")
    @set:PropertyName("second_name")
    var secondName: String? = null,

    @get:PropertyName("phone_number")
    @set:PropertyName("phone_number")
    var phoneNumber : String? = null,

    var street : String? = null,
    var home : String? = null,
    var flat : String? = null,

    var email : String? = null,
    var sex: String? = null,
    //var country : String? = null
)
