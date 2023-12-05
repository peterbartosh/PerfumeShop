package com.example.perfumeshop.presentation.features.login.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SexPickerPreview() {
    val selectedIndState = remember {
        mutableStateOf(1)
    }
    SexPicker(selectedIndState = selectedIndState)
}


@Composable
fun SexPicker(selectedIndState : MutableState<Int>) {
    val sexes = listOf("Мужской", "Женский", "Не задано")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Пол:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 5.dp))

        //Spacer(modifier = Modifier.width(10.dp))

        repeat(3){ ind ->
            SegmentButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(end = 5.dp),
                text = sexes[ind],
                ind = ind,
                selectedIndState = selectedIndState,
                contentPadding = PaddingValues(10.dp)
            )

        }
    }
}
