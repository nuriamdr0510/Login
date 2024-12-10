package com.smb.jc_mylogin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smb.e09_login.navigation.Screens
import com.smb.jc_mylogin.screens.login.LoginScreen
import com.smb.e09_login.screens.splash.SplashScreen

import com.smb.jc_mylogin.screens.home.Home



@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.SplashScreen.name
    ) {
        composable(Screens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(Screens.LoginScreen.name){
            LoginScreen(navController = navController)
        }
        composable(Screens.HomeScreen.name){
            Home(navController = navController)
        }

        composable("login_screen") {
            LoginScreen(navController = navController)
        }

        composable("home_screen") {
            Home(navController = navController)
        }


    }


}