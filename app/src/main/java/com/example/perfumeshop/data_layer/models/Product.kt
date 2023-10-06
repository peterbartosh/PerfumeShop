package com.example.perfumeshop.data_layer.models

import com.example.perfumeshop.data_layer.utils.Sex
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName


data class Product(
    @Exclude var id : String? = null,
    var type : String? = null,
    var brand : String? = null,
//    var collection : String? = null,

    var volume: String? = null,

    @get:PropertyName("cash_price")
    @set:PropertyName("cash_price")
    var cashPrice : Double? = null, // add currency converter (BLR, USD, EUR)

    @get:PropertyName("cashless_price")
    @set:PropertyName("cashless_price")
    var cashlessPrice : Double? = null, // add currency converter (BLR, USD, EUR)

    var sex : Sex? = null,

    @get:PropertyName("is_on_hand")
    @set:PropertyName("is_on_hand")
    var isOnHand : Boolean? = null,

    //var amount : Int? = null
)
