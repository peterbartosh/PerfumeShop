package com.example.perfumeshop.presentation.features.search.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.perfumeshop.presentation.features.search.components.secondary.CustomRadioButton


@Composable
fun SortDialog(
    showDialog: MutableState<Boolean>,
    sortPrioritiesInitValue: List<Int>,
    isAscendingInitValue: Boolean,
    onApplyButtonClick: (Boolean, List<Int>, Boolean) -> Unit
) {

    val N = 3

    val labels = listOf("Цене", "Объёму", "Названию")

    val priorities = remember {
        sortPrioritiesInitValue.toList().toMutableStateList()
    }

    val sortStates = remember {
        List(N) {ind -> priorities.contains(ind) }.toMutableStateList()
    }

    val isAscending = remember {
        mutableStateOf(isAscendingInitValue)
    }

    val applySort = remember(priorities.size) {
        mutableStateOf(priorities.isNotEmpty())
    }

    val isAscendingRadioButtonEnabled by remember(priorities.size) {
        mutableStateOf(priorities.isNotEmpty())
    }

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(all = 5.dp)
            ) {

                Text(text = "Сортировать по:", style = MaterialTheme.typography.titleMedium)


                repeat(N){ ind ->
                    Row (
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Spacer(modifier = Modifier.width(10.dp))
                        CustomRadioButton(ind = ind, states = sortStates, priorities = priorities) {
                            sortStates[ind] = !sortStates[ind]
                            if (sortStates[ind])
                                priorities.add(ind)
                            else
                                priorities.remove(ind)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = labels[ind], style = MaterialTheme.typography.bodyMedium)
                    }
                }

                //Spacer(modifier = Modifier.height(20.dp))

                Divider()

                Row (
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        modifier = Modifier.padding(all = 5.dp),
                        enabled = isAscendingRadioButtonEnabled,
                        selected = isAscending.value,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.primary,
                            disabledSelectedColor = Color.LightGray,
                            disabledUnselectedColor = Color.LightGray
                        ),
                        onClick = { isAscending.value = !isAscending.value }
                    )

                    //Spacer(modifier = Modifier.width(5.dp))

                    Text(
                        text = if (isAscending.value) "По возрастанию" else "По убыванию",
                        color = if (isAscendingRadioButtonEnabled) MaterialTheme.colorScheme.onBackground else Color.LightGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                //Spacer(modifier = Modifier.height(30.dp))

                Divider(modifier = Modifier.height(2.dp), color = MaterialTheme.colorScheme.onBackground)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Column(
                        modifier = Modifier.wrapContentSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(
                            onClick = {
                                onApplyButtonClick(isAscending.value, priorities, applySort.value)
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            //border = BorderStroke(width = 1.dp, color = if (applySort.value) Gold else Color.LightGray),
                            modifier = Modifier.wrapContentSize(),
                            contentPadding = ButtonDefaults.ContentPadding
                        ) {
                            Text(text = "Применить", style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            modifier = Modifier.clickable {
                                for (i in sortStates.indices)
                                    sortStates[i] = false
                                priorities.clear()
                                isAscending.value = true
                            },
                            text = "Очистить всё",
                            textDecoration = TextDecoration.Underline,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}