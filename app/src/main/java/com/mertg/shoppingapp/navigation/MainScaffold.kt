package com.mertg.shoppingapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mertg.shoppingapp.view.*


@Composable
fun MainScaffold(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavbar(navController = navController) },
        /*        floatingActionButton = { FloatingActionButtonContent(navController = navController) },
                floatingActionButtonPosition = FabPosition.Center,*/
    ) { innerPadding ->
        HomeScreen(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Preview
@Composable
private fun prev() {
    MainScaffold(navController = rememberNavController())
}