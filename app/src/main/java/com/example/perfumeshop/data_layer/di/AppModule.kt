package com.example.perfumeshop.data_layer.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.perfumeshop.data_layer.mail.EmailSender
import com.example.perfumeshop.data_layer.repositories.FireRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {
    @Singleton
    @Provides
    fun provideFireRepository()
            = FireRepository(queryReview = FirebaseFirestore.getInstance().collection("reviews"),
                            queryProducts = FirebaseFirestore.getInstance().collection("products"),
                            queryUsers = FirebaseFirestore.getInstance().collection("users"),
                             queryOrders = FirebaseFirestore.getInstance().collection("orders"),
                             queryHot = FirebaseFirestore.getInstance().collection("hot"))

    @Singleton
    @Provides
    fun provideEmailSender()
            = EmailSender()
//    @Composable
//    @Singleton
//    @Provides
//    fun provideUserSettings() = UserSettingsImpl(LocalContext.current)

//    @Singleton
//    @Provides
//    fun provideWeatherDao(database: UserDatabase): UserDao
//            = database.weatherDao()
//
//    @Singleton
//    @Provides
//    fun provideAppDatabase(@ApplicationContext context: Context): UserDatabase = Room
//        .databaseBuilder(context, UserDatabase::class.java, "user_database")
//        .fallbackToDestructiveMigration()
//        .build()
}