package com.example.perfumeshop.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.getWidthPercent

@Preview(showBackground = true)
@Composable
fun NothingFound() {
    ErrorComp(imageInd = R.drawable.nothing_found, textInd = R.string.nothing_found)
}

@Preview(showBackground = true)
@Composable
fun ErrorOccurred() {
    ErrorComp(imageInd = R.drawable.error_occured, textInd = R.string.error_occured)
}

@Composable
private fun ErrorComp(imageInd : Int, textInd : Int) {
    val context = LocalContext.current

    val wp = context.getWidthPercent()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            modifier = Modifier
                .width(wp * 35)
                .wrapContentHeight()
                .padding(bottom = 10.dp),
            painter = painterResource(id = imageInd),
            contentDescription = "nothing found",
            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground)
        )

        Text(
            text = stringResource(id = textInd),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}