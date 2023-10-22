package com.example.perfumeshop.data.di

import com.example.perfumeshop.data.mail.EmailSender
import com.example.perfumeshop.data.repository.FireRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.LocalCacheSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
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
            reviewsCollection = repository.collection("reviews")
        )
    }

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