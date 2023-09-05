package com.example.perfumeshop.data_layer.data


import androidx.room.*
import androidx.room.Dao
import com.example.perfumeshop.data_layer.models.Product

import kotlinx.coroutines.flow.Flow

//@Dao
//interface UserDao {
//    @Query("SELECT * from user_tbl")
//    fun getProducts(): Flow<List<Product>>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertProduct(product: Product)
//
//    @Update(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun updateProduct(product: Product)
//
//    @Query("DELETE from user_tbl")
//    suspend fun deleteAllProducts()
//
//    @Delete
//    suspend fun deleteProduct(product: Product)
//
//}