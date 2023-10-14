package com.example.perfumeshop.data.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.round

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

fun getDateTimeString(timestamp: Timestamp?): String {
    if (timestamp == null) return ""
    val offset = ZoneOffset.ofHoursMinutes(+6, +9)
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.time), ZoneId.systemDefault())
    val odt = OffsetDateTime.of(dateTime, offset)

    return odt.toLocalDateTime()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd|HH:mm"))
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}
@Composable
fun <T : Any> rememberMutableStateList(vararg elements : T) : SnapshotStateList<T>{
    return rememberSaveable(
        saver = listSaver(
            save = { stateList ->
                if (stateList.isNotEmpty()) {
                    val first = stateList.first()
                    if (!canBeSaved(first)) {
                        throw IllegalStateException("${first::class} cannot be saved. By default only types which can be stored in the Bundle class can be saved.")
                    }
                }
                stateList.toList()
            },
            restore = { it.toMutableStateList() }
        )
    ) {
        elements.toList().toMutableStateList()
    }
}