package com.example.perfumeshop.presentation.features.auth.login_register_feature.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.presentation.theme.Gold


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    label: String,
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation =
        if (passwordVisibility.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation()

    OutlinedTextField(value = passwordState.value,
                      onValueChange = { passwordState.value = it },
                      label = { Text(text = label) },
                      singleLine = true,
                      textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
                      modifier = modifier
                          .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                          .fillMaxWidth(),
                      enabled = enabled,
                      keyboardOptions = KeyboardOptions(
                          keyboardType = KeyboardType.Password,
                          imeAction = imeAction),
                      visualTransformation = visualTransformation,
                      trailingIcon = { PasswordVisibility(passwordVisibility = passwordVisibility) },
                      keyboardActions = onAction)

}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(),
    valueState: MutableState<String>,
    onValueChange : (String) -> Unit = { valueState.value = it},
    label: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    textStyle: TextStyle = TextStyle(),
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable() (() -> Unit)? = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    OutlinedTextField(value = valueState.value,
                      onValueChange = onValueChange,
                      label = { Text(text = label)},
                      singleLine = isSingleLine,
                      textStyle = textStyle,
                      modifier = modifier.height(63.dp),
                      enabled = enabled,
                      keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
                      keyboardActions = keyboardActions,
                      visualTransformation = visualTransformation,
                      trailingIcon = trailingIcon
    )
}

@Composable
fun SexPicker(selectedInd : MutableState<Int>) {
    val sexes = listOf("Мужской", "Женский", "Не задано")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Пол:")

        Spacer(modifier = Modifier.width(10.dp))
        repeat(3){ ind ->
            SelectButton(text = sexes[ind], ind = ind, selectedInd = selectedInd)
            Spacer(modifier = Modifier.width(7.dp))
        }
    }
}

@Composable
fun SelectButton(
     text : String,
     ind : Int,
     selectedInd : MutableState<Int>,
     optionalOnClick : () -> Unit = {}
) {

    Button(
        onClick = {
            if (selectedInd.value == ind)
                selectedInd.value = -1
            else {
                selectedInd.value = ind
            }
                  optionalOnClick()

                  },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(width = 1.dp, color = if (selectedInd.value == ind) Gold else Color.LightGray),
        contentPadding = ButtonDefaults.ContentPadding,
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {

        Text(text = text, fontSize = 10.sp, color = Color.Black)
    }
}


@Preview(showBackground = true)
@Composable
fun PhoneInput(
    modifier: Modifier = Modifier,
    phone: MutableState<String> = mutableStateOf("22222222"),
    mask: String = " (00) 000-00-00",
    enabled: Boolean = true,
    maskNumber: Char = '0',
    onPhoneChanged: (String) -> Unit = {}
) {

    Row (
        modifier = Modifier.wrapContentHeight(),
         verticalAlignment = Alignment.CenterVertically
    ){

        Box(
            modifier = Modifier
                .height(40.dp)
                .width(85.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RectangleShape)
        ) {

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                Image(
                    painter = painterResource(id = R.drawable.belarus_flag),
                    contentDescription = "bel flag image",
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .height(20.dp)
                        .width(30.dp)
                )

                Text(text = "+375", modifier = Modifier.padding(end = 5.dp))
            }
        }

        InputField(
            modifier = modifier,
            valueState = phone,
            onValueChange = { value -> onPhoneChanged(value.take(mask.count { it == maskNumber })) },
            label = "Телефон",
            enabled = enabled,
            keyboardType = KeyboardType.NumberPassword,
            visualTransformation = PhoneVisualTransformation(mask, maskNumber)
        )
    }
}

class PhoneVisualTransformation(val mask: String, val maskNumber: Char) : VisualTransformation {

    private val maxLength = mask.count { it == maskNumber }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString

            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskNumber) {
                    val nextDigitIndex = mask.indexOf(maskNumber, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskNumber))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}

private class PhoneOffsetMapper(val mask: String, val numberChar: Char) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        var noneDigitCount = 0
        var i = 0
        while (i < offset + noneDigitCount) {
            if (mask[i++] != numberChar) noneDigitCount++
        }
        return offset + noneDigitCount
    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}










