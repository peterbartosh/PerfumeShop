package com.example.perfumeshop.presentation.features.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.perfumeshop.data.utils.OptionType


@Composable
fun OptionsSection(
    sectionTitle : String = "Личные данные",
    options : List<Option> =  listOf(
        Option("Вход / регистрация", OptionType.Edit, null, Icons.Outlined.ArrowForward),
        Option("Понравившиеся", OptionType.Favourite, null, Icons.Outlined.ArrowForward),
        Option("Заказы", OptionType.Orders, null, Icons.Outlined.ArrowForward)
    ),
    onOptionClick: (OptionType) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Divider()

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = sectionTitle, style = MaterialTheme.typography.labelMedium)
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
