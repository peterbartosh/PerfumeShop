package com.example.perfumeshop.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.example.perfumeshop.R
import com.example.perfumeshop.data.user_preferences.PreferencesManager

@Preview
@Composable
fun PlusMinusPreview() {
    PlusMinus(initValue = 10, onValueChange = {})
}

@Composable
fun PlusMinus(
    initValue : Int,
    onValueChange : (Int) -> Unit
) {

    val context = LocalContext.current

    val valueState = rememberSaveable {
        mutableStateOf(initValue.toString())
    }

    Row(
        modifier = Modifier
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            modifier = Modifier
                .size(25.dp)
                .padding(end = 3.dp)
                .clip(CircleShape),
            onClick = {

                try {
                    if (valueState.value.isEmpty()) valueState.value = "0"
                    var amount = valueState.value.toInt()
                    if (amount > 0) amount--
                    valueState.value = amount.toString()
                    onValueChange(amount)

                } catch (_: Exception) {}
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.minus_icon),
                contentDescription = "minus icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        var isFocused by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(key1 = valueState.value, key2 = isFocused){
            Log.d("DKOSJIO", "PlusMinus: ${valueState.value} ${isFocused}")
            if (valueState.value.isEmpty() && !isFocused)
                valueState.value = "0"
        }

        val preferencesManager = remember {
            PreferencesManager(context)
        }

        val textColor = remember(preferencesManager.getThemeData(0)) {
            mutableStateOf(
                if (preferencesManager.getThemeData(0) == 1)
                    Color(0xFF212121)
                else
                    Color(0xFFFDF5E2)
            )
        }

        BasicTextField(
            modifier = Modifier
                .width(50.dp)
                .height(30.dp)
                //.wrapContentWidth()
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(5.dp)
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            //.padding(start = 5.dp, end = 5.dp),
            textStyle = TextStyle(fontSize = 15.sp, textAlign = TextAlign.Center, color = textColor.value),
            value = valueState.value,
            onValueChange = { newVal ->
                if (newVal.isDigitsOnly() || newVal.isEmpty())
                    valueState.value = newVal
                try {
                    val amount = newVal.toInt()
                    onValueChange(amount)
                } catch (_ : Exception) {}
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
        ){ innerTextField ->
            Row(
                //modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                innerTextField()
            }
        }



        IconButton(
            modifier = Modifier
                .size(25.dp)
                .padding(start = 3.dp)
                .clip(CircleShape),
            onClick = {
                try {
                    if (valueState.value.isEmpty()) valueState.value = "0"
                    var amount = valueState.value.toInt()
                    amount++
                    valueState.value = amount.toString()
                    onValueChange(amount)
                } catch (_: Exception) {}
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.plus_icon),
                contentDescription = "plus icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}