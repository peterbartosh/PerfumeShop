package com.example.perfumeshop.presentation.features.profile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.R
import com.example.perfumeshop.data.utils.OptionType
import com.example.perfumeshop.data.utils.composeIntent
import com.example.perfumeshop.data.utils.showToast


data class Option(val title : String, val type : OptionType, val leadingIcon : Any?, val content : Any?)

@Composable
fun OptionRow(option : Option, onOptionClick: (OptionType) -> Unit) {

    val context = LocalContext.current

    val clipboardManager = LocalClipboardManager.current

    Card(modifier = Modifier
        .fillMaxWidth(0.95f)
        .wrapContentHeight()
        .padding(start = 5.dp, end = 5.dp, top = 2.dp, bottom = 2.dp)
        .clip(RoundedCornerShape(10.dp))
        //.border(width = 1.dp, color = Color.Black)
        .clickable {
            onOptionClick(option.type)
            composeIntent(context, option.type)
        },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
         colors = CardDefaults.cardColors(
             containerColor = MaterialTheme.colorScheme.primaryContainer,
             contentColor = MaterialTheme.colorScheme.onBackground
         ),
        //shape = RoundedCornerShape(3.dp),
         border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(all = 10.dp)
        ) {


            Row(
                modifier = Modifier.padding(start = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                when (option.leadingIcon) {
                    null -> {
                        Box(modifier = Modifier.size(25.dp))
                    }
                    is ImageVector -> Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = option.leadingIcon,
                        contentDescription = "icon"
                    )

                    is Int -> Icon(
                        modifier = Modifier.size(25.dp),
                        painter = painterResource(id = option.leadingIcon),
                        contentDescription = "icon"
                    )
                }

                Spacer(modifier = Modifier.width(5.dp))

                Text(text = option.title, style = MaterialTheme.typography.bodyMedium)
            }


            if (option.content != null)
                when (option.content) {
                    is ImageVector -> Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 5.dp),
                        imageVector = option.content,
                        contentDescription = "arrow front"
                    )

                    is String -> Text(
                        text = option.content,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        //fontSize = 12.sp,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .clickable {
                                clipboardManager.setText(AnnotatedString(option.content))
                                context.showToast(R.string.copied_to_clipboard)
                            }
                    )
                }

        }
    }
}


