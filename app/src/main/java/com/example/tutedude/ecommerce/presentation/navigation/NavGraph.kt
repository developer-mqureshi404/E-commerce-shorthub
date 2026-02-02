package com.example.tutedude.ecommerce.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tutedude.ecommerce.presentation.admin.AdminScreen
import com.example.tutedude.ecommerce.presentation.auth.LoginScreen
import com.example.tutedude.ecommerce.presentation.auth.SignUpScreen
import com.example.tutedude.ecommerce.presentation.cart.CartScreen
import com.example.tutedude.ecommerce.presentation.checkout.CheckoutScreen
import com.example.tutedude.ecommerce.presentation.home.HomeScreen
import com.example.tutedude.ecommerce.presentation.product.ProductDetailScreen
import com.example.tutedude.ecommerce.presentation.upload.UploadProductScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.ProductDetail.route + "/{productId}") {
            ProductDetailScreen(navController = navController)
        }

        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(navController = navController)
        }

        composable(Screen.Upload.route) {
            UploadProductScreen(navController = navController)
        }

        composable(Screen.Admin.route) {
            AdminScreen(navController = navController)
        }
    }
}