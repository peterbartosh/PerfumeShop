package com.example.perfumeshop.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartProductEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "num")
    val num: Int = 0,

    @ColumnInfo(name = "product_id")
    val productId: String,

    @ColumnInfo(name = "cash_price_amount")
    var amountCash : Int? = null,

    @ColumnInfo(name = "cashless_price_amount")
    var amountCashless : Int? = null,

    )