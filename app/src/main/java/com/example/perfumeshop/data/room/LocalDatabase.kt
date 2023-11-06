package com.example.perfumeshop.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity

@Database(
    entities = [CartProductEntity::class, FavouriteProductEntity::class, RegistrationRequestEntity::class],
    version = 6,
    //exportSchema = false
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun localDao(): LocalDao
}