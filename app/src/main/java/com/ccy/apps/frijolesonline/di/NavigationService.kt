package com.ccy.apps.frijolesonline.di

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
//import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


class NavigationService constructor(
    context: Context,
) {
    val navController = NavHostController(context).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }
}

//class AnimatedNavigationService constructor(
//    context: Context,
//) {
//    @OptIn(ExperimentalAnimationApi::class)
//    val animatedNavigator= AnimatedComposeNavigator()
//    @OptIn(ExperimentalAnimationApi::class)
//    val animatedNavController = NavHostController(context).apply {
//        navigatorProvider.addNavigator(ComposeNavigator())
//        navigatorProvider.addNavigator(DialogNavigator())
//        navigatorProvider.addNavigator(animatedNavigator)
//    }
//}