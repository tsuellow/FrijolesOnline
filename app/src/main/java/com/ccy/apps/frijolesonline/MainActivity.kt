package com.ccy.apps.frijolesonline

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccy.apps.frijolesonline.presentation.entry_screen.components.EntryScreen
import com.ccy.apps.frijolesonline.presentation.lot_detail.components.LotDetail
import com.ccy.apps.frijolesonline.presentation.lot_list.components.LotList
import com.ccy.apps.frijolesonline.presentation.publish_lot.components.PublishLot
import com.ccy.apps.frijolesonline.presentation.ui.theme.FrijolesOnlineTheme
import com.ccy.apps.frijolesonline.util.Screen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
//        CoroutineScope(Dispatchers.IO).launch {
//           Log.d("testiando",FirebaseDatabaseConnection(Constants.URL_FIREBASE).getFrijolesLots().toString())
//        }
        setContent {
            FrijolesOnlineTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.EntryScreen.route
                    ) {
                        composable(route = Screen.EntryScreen.route) {
                            EntryScreen(navController = navController)
                        }
                        composable(route = Screen.LotList.route) {
                            LotList(navController = navController)
                        }
                        composable(route = Screen.LotDetail.route+"/{root}/{pushId}") {
                            LotDetail(navController = navController)
                        }
                        composable(route = Screen.PublishLot.route) {
                            PublishLot(mainNavController = navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FrijolesOnlineTheme {
        Greeting("Android")
    }
}