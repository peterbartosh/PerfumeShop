package com.example.perfumeshop.data_layer.models

import com.example.perfumeshop.data_layer.utils.Sex
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName


data class Product(
    @Exclude var id : String? = null,
    var type : String? = null,
    var brand : String? = null,
    var collection : String? = null,

    var volume : Int? = null,
    var price : Double? = null, // add currency converter (BLR, USD, EUR)
    var sex : Sex? = null,

    @get:PropertyName("is_on_hand")
    @set:PropertyName("is_on_hand")
    var isOnHand : Boolean? = null,

    @get:PropertyName("photo_links")
    @set:PropertyName("photo_links")
    var photoLinks : List<String>?  = null
) {

// fun toMap(): MutableMap<String, Any> {
//  return mutableMapOf("product_id" to this.id,
//                      "product_type" to this.type,
//                      "product_brand" to this.brand,
//                      "product_brand_collection" to this.brandCollection,
//                      "product_volume" to this.volume,
//                      "product_price" to this.price,
//                      "product_sex" to this.sex,
//                      "product_is_on_hand" to this.isOnHand)
//
// }
}
