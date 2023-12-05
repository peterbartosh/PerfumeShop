package com.example.perfumeshop.presentation.features.search.components.secondary

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IsOnHandOnlyOption(selected : MutableState<Boolean>, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                top = 5.dp,
                bottom = 5.dp,
                //start = 5.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            modifier = Modifier
            //.padding(end = 5.dp)
            ,
            selected = selected.value,
            onClick = { selected.value = !selected.value },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.primary,
                disabledSelectedColor = MaterialTheme.colorScheme.background,
                disabledUnselectedColor = MaterialTheme.colorScheme.background
            )
        )

        //Spacer(modifier = Modifier.width(30.dp))

        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}