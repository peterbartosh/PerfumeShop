package com.example.perfumeshop.data.room


import androidx.room.*
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity

@Dao
interface LocalDao {

    // registration request table

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRegistrationRequest(registrationRequestEntity: RegistrationRequestEntity)

    @Query("Select * From reg_req_table Where phone_number = :phoneNumber")
    suspend fun getRegistrationRequest(phoneNumber: String) : RegistrationRequestEntity?

    @Query("DELETE from reg_req_table")
    suspend fun deleteAllRegistrationRequests()

    // cart products table

    @Query("SELECT * from cart_table Order By num")
    fun getCartProducts(): List<CartProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartProduct: CartProductEntity)

    @Query("Update cart_table Set cash_price_amount = :cashAmount, cashless_price_amount = :cashlessAmount Where product_id = :productId")
    suspend fun updateCartProductAmount(productId: String, cashAmount: Int, cashlessAmount: Int)

    @Query("Delete from cart_table Where product_id = :productId")
    suspend fun deleteCartProduct(productId: String)

    @Query("DELETE from cart_table")
    suspend fun deleteAllInCart()

    // favourite products table

    @Query("SELECT * from fav_table Order By num")
    fun getFavoriteProducts(): List<FavouriteProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(favoriteProduct: FavouriteProductEntity)

    @Query("Update fav_table Set cash_price_amount = :cashAmount And cashless_price_amount = :cashlessAmount Where product_id = :id")
    suspend fun updateFavouriteProductAmount(id: String, cashAmount: Int, cashlessAmount: Int)

    @Query("Delete from fav_table Where product_id = :productId")
    suspend fun deleteFavoriteProduct(productId: String)

    @Query("DELETE from fav_table")
    suspend fun deleteAllInFavorites()

}