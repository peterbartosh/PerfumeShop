package com.example.perfumeshop.presentation.features.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R

@Preview
@Composable
fun BelarusCodeBoxPreview() {
    BelarusCodeBoxPreview()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BelarusCodeBox() {
    Box(
        modifier = Modifier
            .height(40.dp)
            .wrapContentWidth()
            .border(
                width = TextFieldDefaults.UnfocusedBorderThickness,
                color = Color.DarkGray.copy(0.7f),
                shape = RoundedCornerShape(5.dp)
            )
    ) {

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Image(
                painter = painterResource(id = R.drawable.belarus_flag),
                contentDescription = "bel flag image",
                modifier = Modifier
                    .padding(start = 5.dp)
                    .height(20.dp)
                    .wrapContentWidth()
            )

            Text(text = "+375", modifier = Modifier.padding(start = 5.dp, end = 5.dp))
        }
    }
}