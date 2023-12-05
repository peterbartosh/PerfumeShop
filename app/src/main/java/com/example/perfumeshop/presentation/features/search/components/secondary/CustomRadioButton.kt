package com.example.perfumeshop.presentation.features.search.components.secondary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    ind: Int,
    states : SnapshotStateList<Boolean>,
    priorities : SnapshotStateList<Int>,
    onClick: () -> Unit
) {

    TextButton(
        onClick = onClick,
        modifier = Modifier
            //.wrapContentSize()
            .size(35.dp)
            .padding(all = 5.dp),
        //start = 5.dp, end = 5.dp),
        shape = CircleShape,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (states[ind]) MaterialTheme.colorScheme.primary else Color.White
        ),
        contentPadding = PaddingValues(1.dp)
    ) {
        val text = if (states[ind]) (priorities.indexOf(ind) + 1).toString() else ""
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }

}