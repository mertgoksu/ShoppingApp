package com.mertg.shoppingapp.navigation

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
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.LoginPage.route
) {
    val authViewModel = AuthViewModel()
    val productViewModel = ProductViewModel()
    val cartViewModel = CartViewModel()
    val favoritesViewModel = FavoritesViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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
            HomeScreen(navController = navController, viewModel = productViewModel)
        }
        composable(Screen.CartPage.route) {
            CartScreen(navController = navController, viewModel = cartViewModel)
        }
        composable(Screen.FavoritesPage.route) {
            FavoritesScreen(navController = navController, viewModel = favoritesViewModel)
        }
        composable(Screen.ProfilePageScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.EditProfileScreen.route) {
            EditProfileScreen(navController = navController)
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
                viewModel = productViewModel
            )
        }

    }
}
