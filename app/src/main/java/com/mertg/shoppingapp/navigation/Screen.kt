package com.mertg.shoppingapp.navigation

sealed class Screen(val route: String) {
    object LoginPage : Screen("login")
    object RegisterPage : Screen("register")
    object HomePage : Screen("home")
    object CartPage : Screen("cart")
    object FavoritesPage : Screen("favorites")
    object DetailPage : Screen("detail/{item_id}") {
        fun passItemId(itemId: String): String {
            return "detail/$itemId"
        }
    }
    object UploadItem : Screen("upload")
    object ProfilePage : Screen("profile")
}
