package com.example.tutedude.ecommerce.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object Home : Screen("home")
    object ProductDetail : Screen("product_detail")
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object Upload : Screen("upload")
    object Admin : Screen("admin")
}