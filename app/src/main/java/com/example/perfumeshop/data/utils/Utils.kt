package com.example.perfumeshop.data.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.round

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

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0f
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

@Composable
fun <T : Any> rememberSaveableMutableStateList(vararg elements : T) : SnapshotStateList<T>{
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

fun String.firstLetterToUpperCase() : String {
    if (this.isEmpty()) return ""
    val firstLetter = this.first().uppercase()
    return firstLetter + this.substring(1)
}

fun String.allFirstLettersToUpperCase() : String {
    return this.split(" ").joinToString(separator = " "){ it.firstLetterToUpperCase() }
}

fun String.toBrandFormat(defaultValue: String = this) : String {
    val rusAlphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
    var lastItem = ""
    val items = this.split(" ")

    items.forEach { item ->
        if ((item.toCharArray().any{ ch -> ch in rusAlphabet } ||
                    item.contains("(") || item.contains(")") || item.contains("ml", true)) && lastItem.isEmpty()) {
            lastItem = item
        }
    }

    if (lastItem.isEmpty()) return defaultValue.allFirstLettersToUpperCase()

    val lastInd = this.indexOf(lastItem) - 1
    val brand = this.substring(0, if (lastInd == -1) 0 else lastInd).trim()
    return brand.split(" ").joinToString(separator = " ") { it.firstLetterToUpperCase() }
}

fun generateNumber(
    orderId: String?,
    userId : String?
) : String {

    return if (orderId == null || userId == null) {
        val arr = (System.currentTimeMillis() % 1000000).toString().toCharArray()
        arr.shuffle()
        arr.joinToString("")
    }
    else {
        val finalNumber = mutableListOf<Int>()
        // 0-4, 5-9, 10-14, 15-19
        for (i in 0..3) {
            val sub = orderId.substring(i * 5, (i + 1) * 4 + i)
            val digit = sub.map { ch -> ch.code }.sum() % 10
            finalNumber.add(digit)
        }

        finalNumber.add(userId.substring(0, 13).map { it.code }.sum() % 10)
        finalNumber.add(userId.substring(14, 28).map { it.code }.sum() % 10)

        finalNumber.shuffle()

        finalNumber.joinToString("")
    }
}