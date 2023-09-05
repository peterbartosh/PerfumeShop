package com.example.perfumeshop.data_layer.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class User(
    @Exclude
    var id: String? = null,

    @get:PropertyName("user_auth_id")
    @set:PropertyName("user_auth_id")
    var userAuthId: String? = null,

    @get:PropertyName("first_name")
    @set:PropertyName("first_name")
    var firstName: String? = null,

    @get:PropertyName("second_name")
    @set:PropertyName("second_name")
    var secondName: String? = null,

    @get:PropertyName("phone_number")
    @set:PropertyName("phone_number")
    var phoneNumber : String? = null,

    var email : String? = null,
    var sex: String? = null,
    var country : String? = null,
    var products : List<String>? = null
                    ){
//    fun toMap(): MutableMap<String, Any> {
//        return mutableMapOf("user_id" to this.userId,
//                            "first_name" to this.firstName,
//                            "second_name" to this.secondName,
//                            "email" to this.email,
//                            "phone_number" to this.phoneNumber,
//                            "sex" to this.sex,
//                            "country" to this.country,
//                            "products" to this.products)
//    }

}

//user_id : Primary Key Int
//2) first_name
//3) second_name
//4) email
//5) phone_number
//6) sex
//7) country