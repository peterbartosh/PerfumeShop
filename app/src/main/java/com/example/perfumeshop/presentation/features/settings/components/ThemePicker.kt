package com.example.perfumeshop.presentation.features.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.theme.DarkPurple
import com.example.perfumeshop.presentation.theme.Gold

@Preview
@Composable
fun ThemePickerPreview() {
    val chosenTheme = remember {
        mutableStateOf(0)
    }
    ThemePicker(chosenThemeState = chosenTheme)
}


@Composable
fun ThemePicker(chosenThemeState : MutableState<Int>) {

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
                border = if (chosenThemeState.value == ind)
                    BorderStroke(width = 5.dp, color = MaterialTheme.colorScheme.onBackground)
                else
                    BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.background),
                color = backgroundColors[ind],
                iconId = icons[ind],
                ind = ind
            ) {
                if (chosenThemeState.value != ind) chosenThemeState.value = ind
            }
        }
    }
}


@Composable
private fun ThemeButton(
    border: BorderStroke,
    color: Color,
    iconId: Int,
    ind : Int,
    onClick : () -> Unit
) {

    val context = LocalContext.current

    val wp = context.getWidthPercent()


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

