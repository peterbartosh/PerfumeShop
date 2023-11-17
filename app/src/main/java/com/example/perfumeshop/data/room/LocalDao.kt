package com.example.perfumeshop.data.room


import androidx.room.*
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity
import com.example.perfumeshop.data.room.entities.UserDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDao {

    // user data table

    @Query("Select * From user_data_table")
    suspend fun getUserData() : UserDataEntity

    @Update
    suspend fun updateUserData(userDataEntity: UserDataEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserData(userDataEntity: UserDataEntity)

    @Query("DELETE from user_data_table")
    suspend fun deleteUserData()

    // registration request table

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRegistrationRequest(registrationRequestEntity: RegistrationRequestEntity)

    @Query("Select * From reg_req_table Where phone_number = :phoneNumber")
    suspend fun getRegistrationRequest(phoneNumber: String) : RegistrationRequestEntity?

    @Query("DELETE from reg_req_table")
    suspend fun deleteAllRegistrationRequests()

    // cart products table

    @Query("SELECT * from cart_table Order By num ASC")
    fun getCartProducts(): Flow<List<CartProductEntity>>

    @Query("SELECT * from cart_table Order By num ASC")
    fun getCartProductsAsList(): List<CartProductEntity>

    @Query("Select Count(*) From cart_table Where product_id = :productId")
    suspend fun isInCart(productId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartProduct(cartProduct: CartProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInCart(cartProducts: List<CartProductEntity>)

    @Query("Update cart_table Set cash_price_amount = :cashAmount, cashless_price_amount = :cashlessAmount Where product_id = :productId")
    suspend fun updateCartProductAmount(productId: String, cashAmount: Int, cashlessAmount: Int)

    @Query("Update cart_table Set is_on_hand = :isOnHand Where product_id = :productId")
    suspend fun updateCartProductIsOnHand(productId: String, isOnHand: Boolean)

    @Query("Update cart_table Set cash_price = :cashPrice And cashless_price = :cashlessPrice Where product_id = :productId")
    suspend fun updateCartProductPrices(productId: String, cashPrice: Double, cashlessPrice: Double)

    @Query("Delete from cart_table Where product_id = :productId")
    suspend fun deleteCartProduct(productId: String)

    @Query("DELETE from cart_table")
    suspend fun deleteAllInCart()

    // favourite products table

    @Query("SELECT * from fav_table Order By num ASC")
    fun getFavoriteProducts(): Flow<List<FavouriteProductEntity>>

    @Query("SELECT * from fav_table Order By num ASC")
    fun getFavouriteProductsAsList(): List<FavouriteProductEntity>

    @Query("Select Count(*) From fav_table Where product_id = :productId")
    suspend fun isInFavourites(productId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(favoriteProduct: FavouriteProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInFavorites(favoriteProducts: List<FavouriteProductEntity>)

    @Query("Update fav_table Set cash_price_amount = :cashAmount And cashless_price_amount = :cashlessAmount Where product_id = :id")
    suspend fun updateFavouriteProductAmount(id: String, cashAmount: Int, cashlessAmount: Int)

    @Query("Update fav_table Set cash_price = :cashPrice And cashless_price = :cashlessPrice Where product_id = :productId")
    suspend fun updateFavouriteProductPrices(productId: String, cashPrice: Double, cashlessPrice: Double)

    @Query("Update fav_table Set is_on_hand = :isOnHand Where product_id = :productId")
    suspend fun updateFavouriteProductIsOnHand(productId: String, isOnHand: Boolean)

    @Query("Delete from fav_table Where product_id = :productId")
    suspend fun deleteFavoriteProduct(productId: String)

    @Query("DELETE from fav_table")
    suspend fun deleteAllInFavorites()

}