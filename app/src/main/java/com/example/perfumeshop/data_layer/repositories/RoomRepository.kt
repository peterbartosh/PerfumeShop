package com.example.perfumeshop.data_layer.repositories

import com.example.perfumeshop.data_layer.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class RoomRepository @Inject constructor(private val userDao: UserDao) {
//    fun getFavorites(): Flow<List<Product>> = userDao.getProducts()
//    suspend fun insertFavorite(product: Product) = userDao.insertProduct(product)
//    suspend fun updateFavorite(product: Product) = userDao.updateProduct(product)
//    suspend fun deleteAllFavorites() = userDao.deleteAllProducts()
//    suspend fun deleteFavorite(product: Product) = userDao.deleteProduct(product)
//}