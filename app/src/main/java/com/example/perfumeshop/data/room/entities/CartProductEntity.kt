package com.example.perfumeshop.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.perfumeshop.data.model.Product
import com.example.perfumeshop.data.model.ProductWithAmount

@Entity(tableName = "cart_table")
data class CartProductEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "num")
    override val num: Int = 0,

    @ColumnInfo(name = "product_id")
    override val productId: String,

    @ColumnInfo(name = "type")
    override var type : String? = null,

    @ColumnInfo(name = "brand")
    override var brand : String? = null,

    @ColumnInfo(name = "volume")
    override var volume: Double? = null,

    @ColumnInfo(name = "cash_price")
    override var cashPrice : Double? = null,

    @ColumnInfo(name = "cashless_price")
    override var cashlessPrice : Double? = null,

    @ColumnInfo(name = "is_on_hand")
    override var isOnHand : Boolean? = null,

    @ColumnInfo(name = "cash_price_amount")
    override var cashPriceAmount: Int? = null,

    @ColumnInfo(name = "cashless_price_amount")
    override var cashlessPriceAmount : Int? = null,

): ProductEntity(num, productId, type, brand, volume, cashPrice, cashlessPrice, isOnHand, cashPriceAmount, cashlessPriceAmount)

fun CartProductEntity.toProductWithAmount() =
    ProductWithAmount(
        product = Product(productId, type, brand, volume, cashPrice, cashlessPrice, isOnHand),
        cashPriceAmount = this.cashPriceAmount,
        cashlessPriceAmount = this.cashlessPriceAmount
    )