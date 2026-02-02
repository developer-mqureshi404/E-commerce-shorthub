package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.local.CartDao
import com.example.tutedude.ecommerce.data.model.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepository @Inject constructor(
    private val cartDao: CartDao
) {
    fun getAllCartItems(): Flow<List<CartItem>> = cartDao.getAllCartItems()

    suspend fun getCartItemById(productId: String): CartItem? =
        cartDao.getCartItemById(productId)

    suspend fun addToCart(cartItem: CartItem) {
        val existingItem = cartDao.getCartItemById(cartItem.productId)
        if (existingItem != null) {
            cartDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + 1))
        } else {
            cartDao.insertCartItem(cartItem)
        }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        cartDao.updateCartItem(cartItem)
    }

    suspend fun removeFromCart(cartItem: CartItem) {
        cartDao.deleteCartItem(cartItem)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
    }

    fun getCartItemCount(): Flow<Int> = cartDao.getCartItemCount()
}