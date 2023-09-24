package com.ccy.apps.frijolesonline.presentation.common_components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuTopAppBar(title: String, dropdownItems: @Composable (ColumnScope.() -> Unit)) {

    var expanded by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(
                onClick = {
                    expanded = true
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "menu"
                )
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, content = dropdownItems)
        }
    )

}