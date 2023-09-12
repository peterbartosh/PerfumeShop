package com.example.perfumeshop.ui_layer.features.main.children.profile.children.profile.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.perfumeshop.data_layer.utils.OptionType


data class Option(val title : String, val type : OptionType, val leadingIcon : Any?, val content : Any?)

// theme  auth  about  contacts (phone number, whatsapp, telegram, gmail)
@Composable
fun OptionRow(option : Option = Option(
    "Телефон",
    OptionType.PhoneNumber,
    Icons.Default.Phone,
    "+111111111111_link"),
              onOptionClick: (OptionType) -> Unit
) {

    Card(modifier = Modifier
        .fillMaxWidth(0.9f)
        .height(35.dp)
        .padding(top = 2.dp, bottom = 2.dp)
        // .border(width = 2.dp, color = Color.Black)
        .clickable { onOptionClick(option.type) },
        //.shadow(elevation = 3.dp, shape = RoundedCornerShape(5.dp))
        //.padding(start = 10.dp, end = 10.dp),
         colors = CardDefaults.cardColors(containerColor = Color.White),
         shape = RoundedCornerShape(3.dp),
         border = BorderStroke(1.dp, Color.Black)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {


            if (option.leadingIcon != null)
                Row(modifier = Modifier.padding(start = 5.dp)) {
                    if (option.leadingIcon is ImageVector)
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = option.leadingIcon,
                            contentDescription = "icon"
                        )
                    else if (option.leadingIcon is Painter)
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = option.leadingIcon,
                            contentDescription = "icon"
                        )

                    Spacer(modifier = Modifier.width(3.dp))

                    Text(text = option.title, fontSize = 15.sp)
                }
            else
                Text(text = option.title, fontSize = 15.sp,
                     modifier = Modifier.padding(start = 5.dp))

            if (option.content != null)
                when (option.content) {
                    is ImageVector -> Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 5.dp),
                        imageVector = option.content,
                        contentDescription = "arrow front"
                    )

                    is String -> Text(text = option.content,
                                      fontSize = 12.sp,
                                      modifier = Modifier
                                          .padding(end = 5.dp)
                                          .clickable {})
                }

        }
    }
}


@Composable
fun OptionsSection(
    sectionTitle : String = "Личные данные",
    options : List<Option> =  listOf(
        Option("Вход / регистрация", OptionType.Edit, null, Icons.Outlined.ArrowForward),
        Option("Понравившиеся", OptionType.Favourite, null, Icons.Outlined.ArrowForward),
        Option("Заказы", OptionType.Orders, null, Icons.Outlined.ArrowForward)
    )
    , onOptionClick: (OptionType) -> Unit
) {

    Column(modifier = Modifier
        .padding(10.dp)
        //.fillMaxWidth()
        .wrapContentSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = sectionTitle, fontSize = 25.sp)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(top = 10.dp)
        ) {

            for (option in options)
                OptionRow(option = option , onOptionClick = onOptionClick)

        }
    }
}
