package com.mertg.shoppingapp.navigation

import DetailScreen
import FavoritesViewModel
import ProductViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.mertg.shoppingapp.view.*
import com.mertg.shoppingapp.viewmodel.*


@Composable
fun MainScaffold(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel
) {
    val cartViewModel = CartViewModel()
    val favoritesViewModel = FavoritesViewModel()

    Scaffold(
        bottomBar = { BottomNavbar(navController = navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.LoginPage.route,
            modifier = Modifier.padding(innerPadding)
        ) {
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
                HomeScreen(navController = navController)
            }
            composable(Screen.CartPage.route) {
                CartScreen(navController = navController, viewModel = cartViewModel)
            }
            composable(Screen.FavoritesPage.route) {
                FavoritesScreen(navController = navController, viewModel = favoritesViewModel)
            }
            composable(Screen.ProfilePage.route) {
                //ProfileScreen(navController = navController)
            }
            composable(
                route = Screen.DetailPage.route,
                arguments = listOf(navArgument("itemId") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                DetailScreen(
                    navController = navController,
                    productId = backStackEntry.arguments?.getString("itemId") ?: "",
                    viewModel = productViewModel)
            }
        }
    }
}
