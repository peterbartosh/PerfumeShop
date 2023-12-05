package com.example.perfumeshop.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.showToast
import com.example.perfumeshop.presentation.theme.Gold

@Preview
@Composable
fun SubmitButtonPreview() {
    SubmitButton(text = "text") {}
}

@Composable
fun SubmitButton(
    text: String,
    validInputsState: Boolean? = null,
    onClick: () -> Unit
) {

    val context = LocalContext.current

    Button(
        onClick = {
            if (validInputsState != false)
                onClick()
            else
                context.showToast(R.string.incorrect_input_error)
        },
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Gold),
        enabled = validInputsState ?: true,
        shape = CircleShape
    ) {
        Text(text = text, modifier = Modifier.padding(5.dp), style = MaterialTheme.typography.bodyMedium)
    }

}