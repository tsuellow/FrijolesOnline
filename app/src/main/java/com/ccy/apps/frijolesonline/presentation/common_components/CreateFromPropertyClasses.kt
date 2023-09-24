package com.ccy.apps.frijolesonline.presentation.common_components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ccy.apps.frijolesonline.common.PropertyClass
import com.ccy.apps.frijolesonline.common.PropertyType

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CreateFromPropertyClasses(
    modifier: Modifier = Modifier,
    propertyMap: LinkedHashMap<String, PropertyClass>,
    produceMap: SnapshotStateMap<String, Any?>,
    errorMap: SnapshotStateMap<String, String>,
    submit: () -> Unit,
    onPropertyEdited: (key: String, value: Any?) -> Unit,
    onErrorFound: (key: String, error: String) -> Unit,
    checkInputs: Boolean = false
) {
//    var checkInputs by rememberSaveable{
//        mutableStateOf(false)
//    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(propertyMap.map { Pair(it.key, it.value) }) { property ->
                PropertyField(
                    propertyPair = property,
                    error = errorMap[property.first]!!,
                    value = produceMap[property.first],
                    onValueChanged = { key, value ->
                        onPropertyEdited(key, value)
                        if (checkInputs) {
                            onErrorFound(key, property.second.hasError(value.toString()))
                        }
                    })
            }
            item {
                Spacer(modifier = Modifier.height(18.dp))
                OutlinedButton(onClick = {
//                    checkInputs = true
//                    propertyMap.forEach { (key, property) ->
//                        onErrorFound(key, property.hasError(produceMap[key]?.toString() ?: ""))
//                    }
                    submit()
                }) {
                    Text(modifier = Modifier.padding(start = 8.dp),text = "next")
                    Icon(imageVector = Icons.Default.ArrowForward,
                        contentDescription = "next" )
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyField(
    propertyPair: Pair<String, PropertyClass>,
    error: String,
    value: Any?,
    onValueChanged: (String, Any?) -> Unit,
    enabled: Boolean = true
    //setError: (String, String) -> Unit,
    //triggerSubmission: Boolean,
    //onSubmit: (String, Any?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.weight(0.1f))

        when (val type = propertyPair.second.propertyType) {
            is PropertyType.IntRange -> {
                OutlinedTextFieldValidation(
                    enabled = enabled,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    label = {
                        Text(
                            text = propertyPair.second.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        propertyPair.second.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = propertyPair.second.name
                            )
                        }
                    },
                    error = error,
                    value = (value?.toString() ?: ""),
                    onValueChange = { typed ->
                        onValueChanged(
                            propertyPair.first,
                            typed.filter { it.isDigit() }.toIntOrNull()
                        )
                    },
                    suffix = value?.let { { Text(text = type.unit, color = Color.Gray) } }
                )
            }

            is PropertyType.StringCategory -> {

                DropDownTextFieldValidation(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    enabled = enabled,
                    label = propertyPair.second.name,
                    options = (propertyPair.second.propertyType as PropertyType.StringCategory).listRange,
                    leadingIcon = {
                        propertyPair.second.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = propertyPair.second.name
                            )
                        }
                    },
                    error = error,
                    value = (value?.toString() ?: ""),
                    onValueChange = { onValueChanged(propertyPair.first, it) }
                )
            }

            is PropertyType.Dichotomous -> {
                //onValueChanged(propertyPair.first,false)//is false by default even if not touched
                Text(text = propertyPair.second.name)
                Checkbox(
                    checked = (value ?: false) as Boolean,
                    onCheckedChange = { onValueChanged(propertyPair.first, it) })
            }

            is PropertyType.DateTime -> {
                OutlinedTextFieldValidationDatePicker(
                    enabled = enabled,
                    label = {
                        Text(
                            propertyPair.second.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        propertyPair.second.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = propertyPair.second.name
                            )
                        }
                    },
                    value = (value ?: "") as String,
                    error = error,
                    onValueChange = { onValueChanged(propertyPair.first, it) }

                )
            }

            else -> {
                OutlinedTextFieldValidation(
                    //modifier=Modifier.height(200.dp),
                    enabled = enabled,
                    label = {
                        Text(
                            propertyPair.second.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        propertyPair.second.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = propertyPair.second.name
                            )
                        }
                    },
                    minLines=3,
                    error = error,
                    value = value?.toString() ?: "",
                    onValueChange = {
                        onValueChanged(propertyPair.first, it)
                    }
                )
            }
        }
        //help button

        propertyPair.second.helpText?.let {
            var showDialog by remember {
                mutableStateOf(false)
            }
            IconButton(
                onClick = { showDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Help,
                    contentDescription = "help"
                )
            }
            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = { showDialog = false },
                            content = { Text(text = "OK") })
                    },
                    icon = {
                        propertyPair.second.icon?.let { icon ->
                            Icon(
                                imageVector = icon,
                                contentDescription = propertyPair.second.name
                            )
                        }
                    },
                    title = { Text(text = "What is ${propertyPair.second.name}?") },
                    text = { Text(text = it) })
            }
        } ?: Spacer(modifier = Modifier.width(48.dp))

    }

}