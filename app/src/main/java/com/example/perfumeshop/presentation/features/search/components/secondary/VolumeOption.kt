package com.example.perfumeshop.presentation.features.search.components.secondary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.presentation.theme.Gold


@Composable
fun VolumeOption(states: SnapshotStateList<Double>, volumes : List<Double>, isCompactQueryType: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 5.dp, top = 5.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(modifier = Modifier.padding(end = 5.dp), text = "Объему:", style = MaterialTheme.typography.bodyMedium)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(5.dp)
        ){
            itemsIndexed(volumes){ ind, volume ->
                VolumeButton(volume = volume, states = states, isCompactQueryType = isCompactQueryType)
            }
        }

    }
}

@Composable
private fun VolumeButton(
    volume: Double,
    states: SnapshotStateList<Double>,
    isCompactQueryType: Boolean
) {

    Button(
        onClick = {
            if (states.contains(volume))
                states.remove(volume)
            else
                states.add(volume)
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(
            width = if (states.contains(volume)) 2.dp else 1.dp,
            color = if (states.contains(volume)) Gold else Color.LightGray
        ),
        contentPadding = PaddingValues(5.dp),
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        val volumeStr = if (isCompactQueryType && volume == 60.0) "3x20" else volume.toInt().toString()

        Text(text = volumeStr, fontSize = 10.sp, color = Color.Black)
    }
}