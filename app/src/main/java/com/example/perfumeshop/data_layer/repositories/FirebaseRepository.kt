package com.example.perfumeshop.data_layer.repositories


import android.util.Log
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.Review
import com.example.perfumeshop.data_layer.models.User
import com.example.perfumeshop.data_layer.utils.QueryType
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class FireRepository @Inject constructor(
    private val queryProducts: Query,
    private val queryUsers: Query,
    private val queryReview: Query
) {

    suspend fun getProductReviews(productId : String) : Flow<List<Review>> = flow {
        emit(
            queryReview.whereIn("product_id", mutableListOf(productId))
                .get().await().documents.asFlow().map { ds -> ds.toObject(Review::class.java)!! }.toList()
        )
    }

//    suspend fun getUserProductsFromDatabase(userId : String): Flow<Product> {
////
////        val userProductsCollection =  queryUserProducts.get().await().documents.filter {
////            it.get("userId")?.equals(userId) ?: false
////        }
////
////        return queryProducts.get().await().documents.asFlow()
////            .filter {doc ->
////                userProductsCollection.find { it.get("product_id")?.equals(doc.get("product_id")) == true } != null
////            }
////            .map { documentSnapshot ->
////                documentSnapshot.toObject(Product::class.java)!!
////            }
//    }

    suspend fun getAllProductsFromDatabase() : Flow<Product> {
        return queryProducts.get().await().documents.asFlow().map { ds -> ds.toObject(Product::class.java)!! }
    }


    suspend fun getUserCartProducts(userId : String) : Flow<List<Product>>? {

        Log.d("tttttttt", "getUserCartProducts: 1")

        val userProductsIds : List<String>? = try {

             queryUsers.whereIn("id", mutableListOf(userId))
                .get()
                .await()
                .documents
                .asFlow()
                .map { ds -> ds.toObject(User::class.java)!!.products }
                .first()
        } catch (e : Exception) {
            Log.d("ERROR_ERROR", "getUserCartProducts: ${e.message}")
            null
        }

            if (userProductsIds.isNullOrEmpty()) {
                Log.d("tttttttt", "getUserCartProducts: EEE")
                return null
            }

            val userProducts = listOf( userProductsIds.map { id ->
                queryProducts.whereIn("id", mutableListOf(id)).get().await().documents.first()
            }.map { ds -> ds.toObject(Product::class.java)!!}.toList())

        Log.d("tttttttt", "getUserCartProducts: 3")

            return userProducts.asFlow()
    }

    suspend fun getQueryProducts(query : String, queryType: QueryType) : Flow<List<Product>> = flow{
        emit(queryProducts.whereIn(queryType.name,
                                   mutableListOf(query, query.lowercase(), query.first().uppercase() + query.substring(1)))
                 .get().await().documents.asFlow().map {
                ds -> ds.toObject(Product::class.java)!!
        }.toList())
//        return queryProducts.get().await().documents.asFlow()
//            .map {
//                    ds -> ds.toObject(Product::class.java)!!
//            }.filter {pr ->
//                getPredicateByQuery(queryType = queryType, query = query).test(pr)
//            }
    }


}
