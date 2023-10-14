package com.example.perfumeshop.presentation.features.main.settings_feature.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.features.auth.login_register_feature.ui.SelectButton
import com.example.perfumeshop.presentation.theme.DarkPurple
import com.example.perfumeshop.presentation.theme.Gold
import com.example.perfumeshop.presentation.theme.PreferencesManager


@Composable
fun SettingsScreen(onThemeChange : (Boolean) -> Unit) {

    val preferencesManager = PreferencesManager(LocalContext.current)

    var chosenTheme by remember {
        mutableStateOf(preferencesManager.getData(false))
    }

    val selectedInd = remember {
        mutableStateOf(0)
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Spacer(modifier = Modifier.height(100.dp))

            Row(horizontalArrangement = Arrangement.Center) {

                ThemeButton(
                    onClick = {
                            onThemeChange(true)
                            chosenTheme = !chosenTheme
                    },
                    border = if (chosenTheme)
                                    BorderStroke(width = 7.dp, color = Color.Red)
                                else
                                    BorderStroke(width = 1.dp, color = Color.LightGray),
                    color = DarkPurple,
                    iconId = R.drawable.night
                )

                Spacer(modifier = Modifier.width(30.dp))

                ThemeButton(
                    onClick = {
                        onThemeChange(false)
                        chosenTheme = !chosenTheme
                              },
                    border = if (!chosenTheme)
                                BorderStroke(width = 7.dp, color = Color.Red)
                            else
                                BorderStroke(width = 1.dp, color = Color.LightGray),
                    color = Gold,
                    iconId = R.drawable.sun
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            CurrencyConverter(selectedInd)

        }

    }

}

@Composable
fun ThemeButton(
    onClick : () -> Unit,
    border : BorderStroke,
    color : Color,
    iconId : Int
) {

        Button(
            onClick = onClick,
            enabled = true,
            border = border,
            colors = ButtonDefaults.buttonColors(containerColor = color),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.size(100.dp)
        ) {
            Icon(
                modifier = Modifier.size(70.dp),
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = Color.White
            )
        }
}

@Composable
fun CurrencyConverter(selectedInd : MutableState<Int>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center
    ) {
        val texts = listOf("BYN", "USD", "EUR")
        val iconsIds = listOf(R.drawable.br, R.drawable.dollar, R.drawable.euro)
        repeat(3){ ind ->
            CurrencyButton(text = texts[ind], ind = ind, selectedInd = selectedInd, iconInd = iconsIds[ind])
            Spacer(modifier = Modifier.width(5.dp))
        }
    }

}

@Composable
fun CurrencyButton(text : String, ind : Int, selectedInd : MutableState<Int>, iconInd : Int) {

    Button(
        onClick = {
            if (selectedInd.value == ind)
                selectedInd.value = -1
            else
                selectedInd.value = ind

        },
        contentPadding = PaddingValues(1.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = if (selectedInd.value == ind) Gold else Color.LightGray),
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (ind == 0)
                Text(text = "Br",
                     fontWeight = FontWeight.ExtraBold,
                     color = Gold,
                     fontSize = 17.sp,
                     modifier = Modifier
                         .width(20.dp)
                         .height(23.dp),
                     textAlign = TextAlign.Start
                )
            else
                Icon(
                    painter = painterResource(id = iconInd),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Gold
                )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = text, fontSize = 10.sp, color = Color.Black)
        }
    }
}
