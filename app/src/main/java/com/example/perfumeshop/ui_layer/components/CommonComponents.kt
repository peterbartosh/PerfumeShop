package com.example.perfumeshop.ui_layer.components

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.ui_layer.theme.Gold

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


@Composable
fun SubmitButton(
    text: String,
    validInputsState: MutableState<Boolean>? = null,
    onClick: () -> Unit
) {

    val context = LocalContext.current
    Button(
        onClick = {
            if (validInputsState?.value != false)
                onClick()
            else
                showToast(context, "Введены некорректные данные!")

        },
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Gold),
        enabled = validInputsState?.value ?: true,
        shape = CircleShape
    ) {
        Text(text = text, modifier = Modifier.padding(5.dp))
    }

}


