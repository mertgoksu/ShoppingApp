package com.mertg.shoppingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.mertg.shoppingapp.view.*
import com.mertg.shoppingapp.viewmodel.AuthViewModel

@Composable
fun NavGraph(navController: NavHostController, viewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = Screen.LoginPage.route) {
        composable(Screen.LoginPage.route) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.RegisterPage.route) {
            RegisterScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.HomePage.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.CartPage.route) {
            CartScreen(navController = navController)
        }
        composable(Screen.FavoritesPage.route) {
            FavoritesScreen(navController = navController)
        }
        composable(
            route = Screen.DetailPage.route,
            arguments = listOf(navArgument("item_id") {
                type = NavType.StringType
            })
        ) {
            DetailScreen(navController = navController)
        }
    }
}
