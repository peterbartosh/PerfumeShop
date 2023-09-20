package com.example.perfumeshop.data_layer.utils

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data_layer.models.Product
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.function.Predicate


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
//sealed class TaskState(loading : Boolean? = null, success : Boolean ? = null){
//    class Success() : TaskState()
//    class Failed(e : Exception) : TaskState()
//    class InProgress() : TaskState()
//}



//fun createProducts(collectionName : String = "hot") {
//    for (i in 0..20) {
//        val product = Product(type = "type$i", volume = i*5, brand = "Tom Ford", collection = "Mustang",
//                              price = Random.nextDouble(5.0, 40.0), sex = if (i % 2 == 0) Sex.Male else Sex.Female, isOnHand = true)
//        saveToFirebase(product, collectionName = collectionName)
//    }
//}

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
@RequiresApi(Build.VERSION_CODES.O)
fun getDateTimeTimestamp(timestamp: Timestamp?) : Timestamp {
    if (timestamp == null)
        return Timestamp.valueOf(LocalDateTime.now().toString())
    val offset = ZoneOffset.ofHoursMinutes(+6, +9)
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.time), ZoneId.systemDefault())
    val odt = OffsetDateTime.of(dateTime, offset)
    val output = odt.toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd|HH:mm"))

    return Timestamp.valueOf(odt.toString())
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDateTimeString(timestamp: Timestamp?): String {
    if (timestamp == null) return ""
    val offset = ZoneOffset.ofHoursMinutes(+6, +9)
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.time), ZoneId.systemDefault())
    val odt = OffsetDateTime.of(dateTime, offset)

    return odt.toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd|HH:mm"))
}