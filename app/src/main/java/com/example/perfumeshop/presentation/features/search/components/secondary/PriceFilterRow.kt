package com.example.perfumeshop.presentation.features.search.components.secondary

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.data.user_preferences.PreferencesManager
import com.example.perfumeshop.data.utils.Constants
import com.example.perfumeshop.data.utils.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceFilterRow(vararg priceStates : MutableState<String>) {

    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context = context)
    }

    val textColor = remember(preferencesManager.getThemeData(0)) {
        mutableStateOf(
            if (preferencesManager.getThemeData(0) == 1)
                Color(0xFF212121)
            else
                Color(0xFFFDF5E2)
        )
    }

    val focusStates = remember {
        mutableStateListOf(false, false)
    }

    val placeHolderTexts = listOf("От", "До")

    val extremeValues = listOf("0.0", Constants.MAX_PRODUCT_PRICE.toString())

    val defaultRange = 0f..(Constants.MAX_PRODUCT_PRICE)

    var sliderPosition by remember {
        val range = try {
            val minVal = priceStates[0].value.toFloat().round(1)
            val maxVal = (priceStates[1].value.toFloat()).round(1)
            (minVal..maxVal)
        } catch (e : Exception){
            Log.d("TAG", "PriceFilterRow: $e ${e.message}")
            defaultRange
        }
        mutableStateOf(range)
    }

    LaunchedEffect(key1 = sliderPosition){
        if (sliderPosition != defaultRange) {
            priceStates[0].value =
                (sliderPosition.start.toDouble()).round(1).toString()
            priceStates[1].value =
                (sliderPosition.endInclusive.toDouble()).round(1).toString()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 5.dp, top = 10.dp, bottom = 5.dp),
        //verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            modifier = Modifier.padding(top = 5.dp),
            text = "Цене:",
            style = MaterialTheme.typography.bodyMedium
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 5.dp, end = 5.dp),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                //.padding(start = 5.dp, top = 10.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                repeat(2) { ind ->
                    BasicTextField(
                        modifier = Modifier
                            .height(30.dp)
                            .width(80.dp)
                            .padding(start = 5.dp)
                            .border(
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.onBackground
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .onFocusChanged { focus ->
                                focusStates[ind] = focus.isFocused
                            },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            color = textColor.value
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (ind == 0) ImeAction.Next else ImeAction.Done
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        value = if (priceStates[ind].value == extremeValues[ind])
                            if (focusStates[ind]) "" else placeHolderTexts[ind]
                        else
                            priceStates[ind].value,
                        onValueChange = { priceStates[ind].value = it }
                    ) { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            innerTextField()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            RangeSlider(
                value = sliderPosition,
                valueRange = defaultRange,
                onValueChange = { newRange ->
                    sliderPosition = newRange
                },
            )
        }

    }
}