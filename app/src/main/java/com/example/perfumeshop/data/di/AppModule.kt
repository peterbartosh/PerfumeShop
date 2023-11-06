package com.example.perfumeshop.data.di

import android.content.Context
import androidx.room.Room
import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.repository.FireRepository
import com.example.perfumeshop.data.room.LocalDao
import com.example.perfumeshop.data.room.LocalDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
    fun provideWeatherDao(localDatabase: LocalDatabase): LocalDao
            = localDatabase.localDao()

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
    fun provideEmailSender() = EmailSender()
}