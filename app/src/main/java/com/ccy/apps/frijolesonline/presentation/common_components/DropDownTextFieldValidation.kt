package com.ccy.apps.frijolesonline.presentation.common_components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.Placeholder
import com.ccy.apps.frijolesonline.common.PropertyType
import kotlinx.coroutines.coroutineScope
import kotlin.math.exp

fun Modifier.cancelOnExpandedChangeIfScrolling() = pointerInput(Unit) {
    awaitEachGesture {
        var event: PointerEvent
        var startPosition = Offset.Unspecified
        var cancel = false

        do {
            event = awaitPointerEvent(PointerEventPass.Initial)
            if (startPosition == Offset.Unspecified) {
                startPosition = event.changes.first().position
            }

            val distance = startPosition.minus(event.changes.last().position).getDistance()
            cancel = distance > 10f || cancel
        } while (
            !event.changes.all { it.changedToUp() }
        )

        if (cancel) {
            event.changes.forEach { it.consume() }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTextFieldValidation(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    label: String,
    placeholder:  @Composable() (() -> Unit)? = null,
    options: List<String> = listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5"),
    value: String,
    onValueChange: (String) -> Unit,
    error: String = "",
    leadingIcon: @Composable() (() -> Unit)? = null
) {


    var expanded by remember { mutableStateOf(false) }

    if (options.size < 7) {
        ExposedDropdownMenuBox(
            expanded = expanded && enabled,
            onExpandedChange = {
                expanded = !expanded
            },

            ) {
            OutlinedTextFieldValidation(
                readOnly = true,
                modifier = modifier.menuAnchor(),
                value = value,
                enabled = enabled,
                onValueChange = {},
                label = { Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                placeholder=placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded && enabled
                    )
                },
                error = error
            )
            ExposedDropdownMenu(
                expanded = expanded && enabled,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        },
                        text = {
                            Text(text = selectionOption)
                        }
                    )
                }
            }
        }
    } else {
        OutlinedTextFieldValidation(
            readOnly = true,
            value = value,
            enabled = enabled,
            onValueChange = {},
            label = { Text(label) },
            leadingIcon = leadingIcon,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded && enabled
                )
            },
            error = error,
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect {
                            if (it is PressInteraction.Release) {
                                expanded = !expanded
                            }
                        }
                    }
                }
        )
        if (expanded) {
            AlertDialog(
                modifier = Modifier.fillMaxHeight(0.8f),
                onDismissRequest = { expanded = !expanded },
                confirmButton = {
                    OutlinedButton(onClick = { expanded = false }) {
                        Text(text = "close")
                    }
                },
                title = { Text(text = "Choose $label") },
                text = {
                    Column() {

                        Spacer(modifier = Modifier.height(8.dp))
                        var searchText by remember {
                            mutableStateOf("")
                        }
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "search"
                                )
                            })
                        Spacer(Modifier.height(8.dp))
                        Card(modifier=Modifier.fillMaxHeight()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                items(options.filter {
                                    it.lowercase().removeNonSpacingMarks()
                                        .contains(searchText.removeNonSpacingMarks().lowercase())
                                }) { listItem ->

                                    Text(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .clickable {
                                                onValueChange(listItem)
                                                expanded = false
                                            },
                                        text = listItem,
                                        style = MaterialTheme.typography.bodyLarge,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Bold
                                    )

                                }
                            }
                        }
                    }
                }

            )
        }

    }
}