package com.example.perfumeshop.di

import android.content.Context
import androidx.room.Room
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.repository.RoomRepository
import com.example.perfumeshop.data.room.LocalDao
import com.example.perfumeshop.data.room.LocalDatabase
import com.example.perfumeshop.data.skeleton.CartFunctionality
import com.example.perfumeshop.data.skeleton.DataManager
import com.example.perfumeshop.data.skeleton.FavouriteFunctionality
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// dependencies provider
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): LocalDatabase =
        Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "local_database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideLocalDao(localDatabase: LocalDatabase) = localDatabase.localDao()

    @Singleton
    @Provides
    fun provideRoomRepository(localDao: LocalDao) = RoomRepository(localDao)

    @Singleton
    @Provides
    fun provideFireRepository() : FireRepository {
        val repository = FirebaseFirestore.getInstance()

        return FireRepository(
            productsCollection = repository.collection("products"),
            usersCollection = repository.collection("users"),
            cartCollection = repository.collection("cart"),
            favouriteCollection = repository.collection("favourite"),
            ordersCollection = repository.collection("orders"),
            ordersProductsCollection = repository.collection("orders_products"),
            blackListCollection = repository.collection("black_list"),
        )
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideEmailSender() = EmailSender()

    @Singleton
    @Provides
    fun provideCartFunctionality(roomRepository: RoomRepository) = CartFunctionality(roomRepository)

    @Singleton
    @Provides
    fun provideFavouriteFunctionality(roomRepository: RoomRepository) = FavouriteFunctionality(roomRepository)

    @Singleton
    @Provides
    fun provideDataManager(roomRepository: RoomRepository, fireRepository: FireRepository) =
        DataManager(roomRepository, fireRepository)
}