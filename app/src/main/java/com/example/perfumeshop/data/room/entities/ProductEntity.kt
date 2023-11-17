package com.example.perfumeshop.data.room.entities

open class ProductEntity(
    open val num: Int = 0,
    open val productId: String,
    open var type : String? = null,
    open var brand : String? = null,
    open var volume: Double? = null,
    open var cashPrice : Double? = null,
    open var cashlessPrice : Double? = null,
    open var isOnHand : Boolean? = null,
    open var cashPriceAmount: Int? = null,
    open var cashlessPriceAmount : Int? = null,
)