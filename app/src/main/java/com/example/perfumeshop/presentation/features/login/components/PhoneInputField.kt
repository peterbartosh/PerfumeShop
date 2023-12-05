package com.example.perfumeshop.presentation.features.login.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.presentation.components.InputField

@Preview(showBackground = true)
@Composable
fun PhoneInputFieldPreview() {
    val phone = remember {
        mutableStateOf("22222222")
    }
    PhoneInputField(
        modifier = Modifier,
        phone = phone,
        mask = " (00) 000-00-00",
        enabled = true,
        maskNumber = '0',
        onPhoneChanged = {}
    )
}

@Composable
fun PhoneInputField(
    modifier: Modifier = Modifier,
    phone: MutableState<String> = mutableStateOf("22222222"),
    mask: String = " (00) 000-00-00",
    enabled: Boolean = true,
    maskNumber: Char = '0',
    onPhoneChanged: (String) -> Unit = {}
) {

    Row (
        modifier = modifier
            .wrapContentHeight(),
        //.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ){

        BelarusCodeBox()

        Spacer(modifier = Modifier.width(3.dp))

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

private class PhoneVisualTransformation(private val mask: String, private val maskNumber: Char) :
    VisualTransformation {

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