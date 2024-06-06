package com.mertg.shoppingapp.navigation

sealed class Screen(val route: String) {
    data object LoginPage : Screen(route = "login_route")
    data object RegisterPage : Screen(route = "register_route")
    data object HomePage : Screen(route = "home_route")
    data object CartPage : Screen(route = "cart_route")
    data object FavoritesPage : Screen(route = "favorites_route")
    data object DetailPage : Screen(route = "detail_route/{item_id}") {
        fun passItemId(itemId: String): String {
            return "detail_route/$itemId"
        }
    }
}
