package com.example.perfumeshop.presentation.features.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SegmentButtonPreview() {
    val selectedIndState = remember {
        mutableStateOf(1)
    }
    SegmentButton(text = "Text", ind = 0, selectedIndState = selectedIndState)
}



@Composable
fun SegmentButton(
    modifier : Modifier = Modifier
        .wrapContentWidth()
        .wrapContentHeight(),
    text : String,
    ind : Int,
    defaultInd : Int = -1,
    contentPadding : PaddingValues = PaddingValues(5.dp),
    selectedIndState : MutableState<Int>,
    onOptionalClick : () -> Unit = {},
    shape : Shape = RoundedCornerShape(10.dp),
    borderWidth : Int = 2,
    Content : @Composable () -> Unit = {
        Text(text = text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
    }
) {

    Button(
        modifier = modifier,
        onClick = {
            if (selectedIndState.value == ind)
                selectedIndState.value = defaultInd
            else
                selectedIndState.value = ind

            onOptionalClick()
        },
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(
            width = borderWidth.dp,
            color = if (selectedIndState.value == ind)
                MaterialTheme.colorScheme.primary
            else
                Color.LightGray
            //MaterialTheme.colorScheme.onBackground

        ),
        contentPadding = contentPadding
    ) {

        Content()
    }
}
