package com.example.perfumeshop.data_layer.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data_layer.models.Product
import com.example.perfumeshop.data_layer.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.util.Objects
import java.util.function.Predicate
import kotlin.random.Random


fun getPredicateByQuery(queryType: QueryType, query : String) : Predicate<Product> {
            return when(queryType){
                QueryType.collection -> Predicate{ p ->
                    if (p.collection == null) false
                    else
                    p.collection?.trim()?.lowercase() == query.trim().lowercase() ||
                            p.collection?.trim()?.lowercase()
                                ?.contains(query.trim().lowercase()) ?: false ||
                            query.trim().lowercase().contains(p.collection?.trim()?.lowercase()!!)

                }
                QueryType.type -> Predicate{ p -> p.type == query}
                QueryType.brand -> Predicate{ p ->
                    if (p.brand == null) false
                    else
                        p.brand?.trim()?.lowercase() == query.trim().lowercase() ||
                                p.brand?.trim()?.lowercase()
                                    ?.contains(query.trim().lowercase()) ?: false ||
                                query.trim().lowercase().contains(p.brand?.trim()?.lowercase()!!)

                }
                QueryType.volume -> Predicate{ p ->
                    p.volume in query.split('v')[0].toInt()..query.split('v')[1].toInt()
                }
                QueryType.price -> Predicate{ p ->
                    p.price!! >= query.split('p')[0].toDouble() &&
                            p.price!! <= query.split('p')[1].toDouble()
                }
                QueryType.sex -> Predicate{ p ->
                    query.map { ch -> sexEntities[ch.digitToInt()] }.any { sex -> p.sex == sex }
                }
                QueryType.is_on_hand -> Predicate{ p -> p.isOnHand!! }
            }
        }


fun saveToFirebase(item: Any, collectionName: String) {
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection(collectionName)

    if (item.toString().isNotEmpty()){
        dbCollection.add(item)
            .addOnSuccessListener { documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            //navController.popBackStack()
                        }


                    }.addOnFailureListener {
                        Log.d("Error", "SaveToFirebase:  ${it.message} ")
                    }

            }


    }else {



    }
}



fun deleteFromDatabase(id: String, collectionName: String, onSuccess: () -> Unit) {
    FirebaseFirestore.getInstance()
        .collection(collectionName)
        .document(id)
        .delete()
        .addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess.invoke()
            }
        }
}


fun updateFieldInDatabase(
    collectionPath : String,
    id : String,
    fieldPath : String,
    updatedValue : Any) {

    FirebaseFirestore.getInstance()
        .collection(collectionPath)
        .document(id)
        .update(fieldPath, updatedValue)
        .addOnCompleteListener {
            Log.d("DATABASE_TASK", "updateFieldInDatabase: SUCCESS")
        }.addOnFailureListener{
            Log.d("DATABASE_TASK", "updateFieldInDatabase: FAILED")
        }
}

suspend fun getCurrentUserDatabaseId() : String {
    val auth = FirebaseAuth.getInstance()
    return if (auth.currentUser?.isAnonymous == true)
         ""
    else
        FirebaseFirestore.getInstance()
        .collection("users")
        .whereIn("user_auth_id", listOf(auth.uid))
            .get().await().documents.first().toObject(User::class.java)?.id ?: ""
}

fun createProducts(collectionName : String = "hot") {
    for (i in 0..20) {
        val product = Product(type = "type$i", volume = i*5, brand = "Tom Ford", collection = "Mustang",
                              price = Random.nextDouble(5.0, 40.0), sex = if (i % 2 == 0) Sex.Male else Sex.Female, isOnHand = true)
        saveToFirebase(product, collectionName = collectionName)
    }
}

@Composable
fun getWidthPercent(context: Context): Dp {
    val displayMetrics = context.resources.displayMetrics
    return ((displayMetrics.widthPixels / displayMetrics.density) / 100).dp
}

@Composable
fun getHeightPercent(context: Context): Dp {
    val displayMetrics = context.resources.displayMetrics
    return ((displayMetrics.heightPixels / displayMetrics.density) / 100).dp
}
