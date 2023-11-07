package com.example.perfumeshop.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.perfumeshop.data.room.entities.CartProductEntity
import com.example.perfumeshop.data.room.entities.FavouriteProductEntity
import com.example.perfumeshop.data.room.entities.RegistrationRequestEntity
import com.example.perfumeshop.data.room.entities.UserDataEntity

@Database(
    entities = [
        CartProductEntity::class,
        FavouriteProductEntity::class,
        RegistrationRequestEntity::class,
        UserDataEntity::class
               ],
    version = 8
)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun localDao(): LocalDao
}