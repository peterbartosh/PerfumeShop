package com.example.perfumeshop.ui_layer.features.auth.children.login_register.ui


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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.R
import com.example.perfumeshop.ui_layer.components.LoadingIndicator
import com.example.perfumeshop.ui_layer.theme.Gold


//@Composable
//fun ReaderLogo(modifier: Modifier = Modifier) {
//    Text(
//        modifier = modifier.padding(16.dp),
//        text = "Reader App", style =
//        TextStyle(color = Color.Red, fontSize = 25.sp, fontStyle = FontStyle.Italic)
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    onValueChange : (String) -> Unit = { valueState.value = it},
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    textStyle: TextStyle = TextStyle(),
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable() (() -> Unit)? = {},
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    OutlinedTextField(value = valueState.value,
                      onValueChange = onValueChange,
                      label = { Text(text = labelId)},
                      singleLine = isSingleLine,
                      textStyle = textStyle,
                      modifier = modifier
                          .padding(5.dp)
                          .fillMaxWidth(),
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

    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Пол:")

        Spacer(modifier = Modifier.width(10.dp))
        repeat(3){ ind ->
            SelectButton(text = sexes[ind], ind = ind, selectedInd = selectedInd)
            Spacer(modifier = Modifier.width(7.dp))
        }
    }
}

@Composable
fun SelectButton(text : String, ind : Int, selectedInd : MutableState<Int>) {

    Button(
        onClick = { selectedInd.value = ind },
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
            onValueChange = { onPhoneChanged(it.take(mask.count { it == maskNumber })) },
            labelId = "Телефон",
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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

@Composable
fun SubmitButton(text: String,
                 loading: Boolean,
                 validInputs: Boolean,
                 onClick: () -> Unit) {
    Button(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if (loading) LoadingIndicator()
        else Text(text = text, modifier = Modifier.padding(5.dp))

    }

}










