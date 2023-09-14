package com.example.perfumeshop.ui_layer.components

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingIndicator(progress : MutableState<Float> = mutableStateOf(0.0f)) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Loading...")
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

fun showToast(context : Context, text : String){
    val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
    toast.show()
}


