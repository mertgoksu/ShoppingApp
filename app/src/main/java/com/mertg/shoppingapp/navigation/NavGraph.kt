package com.mertg.shoppingapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.mertg.shoppingapp.util.UploadItem
import com.mertg.shoppingapp.view.*
import com.mertg.shoppingapp.viewmodel.*

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel
) {
    val cartViewModel = CartViewModel()
    val favoritesViewModel = FavoritesViewModel()

    NavHost(navController = navController, startDestination = Screen.LoginPage.route) {
        composable(Screen.LoginPage.route) {
            LoginScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Screen.RegisterPage.route) {
            RegisterScreen(navController = navController, viewModel = authViewModel)
        }
        composable(Screen.UploadItem.route) {
            UploadItem(navController = navController, viewModel = productViewModel)
        }
        composable(Screen.HomePage.route) {
            MainScaffold(navController = navController)
        }
        composable(Screen.CartPage.route) {
            CartScreen(navController = navController, viewModel = cartViewModel)
        }
        composable(Screen.FavoritesPage.route) {
            FavoritesScreen(navController = navController, viewModel = favoritesViewModel)
        }
        composable(Screen.ProfilePage.route) {
            // ProfileScreen composable fonksiyonu burada oluşturulmalı
        }
        composable(
            route = Screen.DetailPage.route,
            arguments = listOf(navArgument("item_id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            DetailScreen(navController = navController, productId = backStackEntry.arguments?.getString("item_id") ?: "", viewModel = productViewModel)
        }
    }
}
