package com.mertg.shoppingapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mertg.shoppingapp.view.*
import com.mertg.shoppingapp.viewmodel.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavController

@Composable
fun ShoppingAppNavigator() {
    val bottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(
                icon = Icons.Default.Home, text = "Home"
            ),
            BottomNavigationItem(
                icon = Icons.Default.Favorite, text = "Favorites"
            ),
            BottomNavigationItem(
                icon = Icons.Default.ShoppingCart, text = "Cart"
            ),
            BottomNavigationItem(
                icon = Icons.Default.Person, text = "Profile"
            )
        )
    }

    val navController = rememberNavController()
    val backstackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    selectedItem = remember(key1 = backstackState) {
        when (backstackState?.destination?.route) {
            Screen.HomePage.route -> 0
            Screen.FavoritesPage.route -> 1
            Screen.CartPage.route -> 2
            Screen.ProfilePageScreen.route -> 3
            else -> 0
        }
    }

    val isBottomBarVisible = remember(key1 = backstackState) {
        backstackState?.destination?.route in listOf(
            Screen.HomePage.route,
            Screen.FavoritesPage.route,
            Screen.CartPage.route,
            Screen.ProfilePageScreen.route
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (isBottomBarVisible) {
                Box {
                    BottomNavigation(
                        items = bottomNavigationItems,
                        selected = selectedItem,
                        onItemClick = { index ->
                            when (index) {
                                0 -> navigateToTab(navController, Screen.HomePage.route)
                                1 -> navigateToTab(navController, Screen.FavoritesPage.route)
                                2 -> navigateToTab(navController, Screen.CartPage.route)
                                3 -> navigateToTab(navController, Screen.ProfilePageScreen.route)
                            }
                        },
                        onFabClick = {
                            navController.navigate(Screen.UploadItem.route)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}

private fun navigateToTab(navController: NavController, route: String) {
    navController.navigate(route) {
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen) { saveState = true }
            restoreState = true
            launchSingleTop = true
        }
    }
}
