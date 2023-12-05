package com.example.perfumeshop.presentation.features.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getWidthPercent
import com.example.perfumeshop.presentation.features.login.components.SegmentButton

@Preview
@Composable
fun FontSizePickerPreview() {
    val chosenFont = remember {
        mutableStateOf(1)
    }
    FontSizePicker(selectedIndState = chosenFont)
}

@Composable
fun FontSizePicker(selectedIndState: MutableState<Int>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val context = LocalContext.current

        val wp = context.getWidthPercent()


        repeat(3) { ind ->

            SegmentButton(
                modifier = Modifier
                    //.wrapContentHeight()
                    .height(wp * 25)
                    .width(wp * 30),
                //.padding(3.dp),
                text = "",
                ind = ind,
                defaultInd = selectedIndState.value,
                selectedIndState = selectedIndState,
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
