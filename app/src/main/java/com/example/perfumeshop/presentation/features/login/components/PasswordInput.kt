package com.example.perfumeshop.presentation.features.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R

@Preview
@Composable
fun PasswordInputPreview() {
    val passwordState = remember {
        mutableStateOf("password")
    }
    val passwordVisibilityState = remember {
        mutableStateOf(false)
    }
    PasswordInputField(
        passwordState = passwordState,
        label = "Label",
        passwordVisibilityState = passwordVisibilityState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInputField(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    label: String,
    enabled: Boolean = true,
    passwordVisibilityState: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation =
        if (passwordVisibilityState.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier
            .padding(top = 5.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibilityState) },
        keyboardActions = onAction,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

}

@Composable
private fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value}) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id =
                                      if (passwordVisibility.value)
                                          R.drawable.hide_password
                                      else
                                          R.drawable.show_password
            ),
            contentDescription = "show/hide pwd"
        )

    }
}
