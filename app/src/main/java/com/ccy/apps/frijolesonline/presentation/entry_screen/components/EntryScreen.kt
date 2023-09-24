package com.ccy.apps.frijolesonline.presentation.entry_screen.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ccy.apps.frijolesonline.Greeting
import com.ccy.apps.frijolesonline.presentation.common_components.MenuTopAppBar
import com.ccy.apps.frijolesonline.presentation.ui.theme.FrijolesOnlineTheme
import com.ccy.apps.frijolesonline.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    navController: NavController
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Frijoles Online")
                },
                navigationIcon = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "Localized description"
                        )
                    }
                },
                //colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            )


}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(it),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = "Hola Fulano", style = MaterialTheme.typography.headlineMedium)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Que querés hacer?")

            Button(onClick = { navController.navigate(Screen.LotList.route) }) {
                Text(text = "Comprar Frijoles")
            }

            Button(onClick = { navController.navigate(Screen.PublishLot.route) }) {
                Text(text = "Vender Frijoles")
            }
        }


    }
}

}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    FrijolesOnlineTheme {
        EntryScreen(navController = rememberNavController())
    }
}