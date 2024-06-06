package com.mertg.shoppingapp.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mertg.shoppingapp.navigation.Screen

@Composable
fun BottomNavbar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 4.dp,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
            selected = false,
            onClick = { navController.navigate(Screen.HomePage.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites", tint = Color.White) },
            selected = false,
            onClick = { navController.navigate(Screen.FavoritesPage.route) }
        )
//        Spacer(modifier = Modifier.weight(1f, true))
        FloatingActionButtonContent(navController = navController) //Orta buton navbar
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White) },
            selected = false,
            onClick = { navController.navigate(Screen.CartPage.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White) },
            selected = false,
            onClick = { navController.navigate(Screen.ProfilePage.route) }
        )
    }
}
@Composable
fun FloatingActionButtonContent(navController: NavController) {
    Box(modifier = Modifier.fillMaxHeight()){
        FloatingActionButton(
            onClick = { navController.navigate(Screen.UploadItem.route) },
            shape = MaterialTheme.shapes.extraLarge,
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(60.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
        }
    }
}




