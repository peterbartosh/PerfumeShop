package com.example.perfumeshop.presentation.features.main.settings_feature.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.user_preferences.PreferencesManager
import com.example.perfumeshop.data.utils.UserPreferencesType
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.SegmentButton
import com.example.perfumeshop.presentation.theme.DarkPurple
import com.example.perfumeshop.presentation.theme.Gold


@Composable
fun SettingsScreen(onUserPreferencesChanged: (UserPreferencesType, Int) -> Unit) {

    val context = LocalContext.current

    val preferencesManager = remember {
        PreferencesManager(context)
    }

    val chosenTheme = remember {
        mutableStateOf(preferencesManager.getThemeData(defaultValue = 0))
    }

    val chosenFontSizeInd = remember {
        mutableStateOf(preferencesManager.getFontSizeData(defaultValue = 1))
    }

    LaunchedEffect(key1 = chosenTheme.value){
        preferencesManager.saveThemeData(value = chosenTheme.value)
        onUserPreferencesChanged(UserPreferencesType.Theme, chosenTheme.value)
    }

    LaunchedEffect(key1 = chosenFontSizeInd.value){
        preferencesManager.saveFontSizeData(value = chosenFontSizeInd.value)
        onUserPreferencesChanged(UserPreferencesType.FontSize, chosenFontSizeInd.value)
    }


    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = "Настройки",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        Divider(modifier = Modifier.padding(top = 30.dp, bottom = 10.dp))

        Text(
            text = "Выбор темы",
            style = MaterialTheme.typography.labelMedium
        )

        Divider(modifier = Modifier.padding(top = 10.dp, bottom = 15.dp))

        ThemePicker(chosenTheme)

        Divider(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))

        Text(text = "Выбор размера шрифта", style = MaterialTheme.typography.labelMedium)

        Divider(modifier = Modifier.padding(top = 10.dp, bottom = 15.dp))

        FontSizePicker(selectedInd = chosenFontSizeInd)

        Divider(modifier = Modifier.padding(top = 15.dp, bottom = 10.dp))

    }
}

@Composable
fun FontSizePicker(selectedInd: MutableState<Int>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val context = LocalContext.current

        val wp = getWidthPercent(context = context)


        repeat(3) { ind ->

            SegmentButton(
                modifier = Modifier
                    //.wrapContentHeight()
                    .height(wp * 25)
                    .width(wp * 30),
                    //.padding(3.dp),
                text = "",
                ind = ind,
                defaultInd = selectedInd.value,
                selectedInd = selectedInd,
                shape = RoundedCornerShape(5.dp),
                borderWidth = 3,
                Content = {
                    Icon(
                        modifier = Modifier
                            .width(40.dp + 10.dp * ind)
                            .wrapContentHeight(),
                            //.padding(3.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(id = R.drawable.font_size),
                        contentDescription = null
                    )
                }
            )
        }

    }
}

@Composable
fun ThemePicker(chosenTheme : MutableState<Int>) {

    val backgroundColors = listOf(DarkPurple, Gold)
    val icons = listOf(R.drawable.night, R.drawable.sun)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(2) { ind ->
            ThemeButton(
                border = if (chosenTheme.value == ind)
                    BorderStroke(width = 5.dp, color = MaterialTheme.colorScheme.onBackground)
                else
                    BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.background),
                color = backgroundColors[ind],
                iconId = icons[ind],
                ind = ind
            ) {
                if (chosenTheme.value != ind) chosenTheme.value = ind
            }
        }
    }
}

@Composable
fun ThemeButton(
    border: BorderStroke,
    color: Color,
    iconId: Int,
    ind : Int,
    onClick : () -> Unit
) {

    val context = LocalContext.current

    val wp = getWidthPercent(context = context)


    Button(
        onClick = onClick,
        enabled = true,
        border = border,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .width(wp * 35)
            .height(wp * 55)
    ) {
        Icon(
            modifier = Modifier
                .wrapContentWidth()
                .padding(5.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.White
        )
    }
}
//
//@Composable
//fun CurrencyConverter(selectedInd : MutableState<Int>) {
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight(),
//        horizontalArrangement = Arrangement.Center
//    ) {
//        val texts = listOf("BYN", "USD", "EUR")
//        val iconsIds = listOf(R.drawable.br, R.drawable.dollar, R.drawable.euro)
//        repeat(3){ ind ->
//            CurrencyButton(text = texts[ind], ind = ind, selectedInd = selectedInd, iconInd = iconsIds[ind])
//            Spacer(modifier = Modifier.width(5.dp))
//        }
//    }
//
//}
//
//@Composable
//fun CurrencyButton(text : String, ind : Int, selectedInd : MutableState<Int>, iconInd : Int) {
//
//    Button(
//        onClick = {
//            if (selectedInd.value == ind)
//                selectedInd.value = -1
//            else
//                selectedInd.value = ind
//
//        },
//        contentPadding = PaddingValues(1.dp),
//        shape = RoundedCornerShape(10.dp),
//        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
//        border = BorderStroke(width = 1.dp, color = if (selectedInd.value == ind) Gold else Color.LightGray),
//        modifier = Modifier
//            .wrapContentWidth()
//            .wrapContentHeight()
//    ) {
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            if (ind == 0)
//                Text(text = "Br",
//                     fontWeight = FontWeight.ExtraBold,
//                     color = Gold,
//                     fontSize = 17.sp,
//                     modifier = Modifier
//                         .width(20.dp)
//                         .height(23.dp),
//                     textAlign = TextAlign.Start
//                )
//            else
//                Icon(
//                    painter = painterResource(id = iconInd),
//                    contentDescription = null,
//                    modifier = Modifier.size(20.dp),
//                    tint = Gold
//                )
//            Spacer(modifier = Modifier.width(5.dp))
//            Text(text = text, fontSize = 10.sp, color = Color.Black)
//        }
//    }
//}
