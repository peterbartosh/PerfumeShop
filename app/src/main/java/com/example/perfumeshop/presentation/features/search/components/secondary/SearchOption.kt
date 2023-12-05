package com.example.perfumeshop.presentation.features.search.components.secondary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun SearchOption(
    applied : Boolean,
    text : String,
    iconId : Int,
    showDialogState : MutableState<Boolean>
) {

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .padding(5.dp)
            .clip(RoundedCornerShape(50.dp))
            .clickable { showDialogState.value = !showDialogState.value },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (applied)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onBackground
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (applied)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onBackground
        )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier
                    .size(27.dp)
                    .padding(start = 5.dp, end = 1.dp)
            )

            Text(
                text = text,
                modifier = Modifier.padding(end = 5.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }

}