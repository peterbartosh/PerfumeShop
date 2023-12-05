package com.example.perfumeshop.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun InputFieldPreview() {
    val state = remember { mutableStateOf("") }
    InputField(valueState = state, label = "label", enabled = true)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 5.dp, end = 5.dp),
    valueState: MutableState<String>,
    onValueChange : (String) -> Unit = { valueState.value = it},
    label: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable() (() -> Unit)? = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    placeholder : @Composable () -> Unit = {}
) {

    OutlinedTextField(
        value = valueState.value,
        onValueChange = onValueChange,
        label = { Text(text = label, style = MaterialTheme.typography.bodyMedium) },
        singleLine = isSingleLine,
        textStyle = textStyle,
        modifier = modifier.height(66.dp),
        enabled = enabled,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        placeholder = placeholder,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

}