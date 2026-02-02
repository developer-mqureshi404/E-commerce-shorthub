package com.example.tutedude.ecommerce.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.CartItem
import com.example.tutedude.ecommerce.data.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _cartTotal = MutableStateFlow(0.0)
    val cartTotal: StateFlow<Double> = _cartTotal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getAllCartItems().collect { items ->
                _cartItems.value = items
                calculateTotal(items)
            }
        }
    }

    private fun calculateTotal(items: List<CartItem>) {
        _cartTotal.value = items.sumOf { it.price * it.quantity }
    }

    fun incrementQuantity(item: CartItem) {
        viewModelScope.launch {
            cartRepository.updateCartItem(item.copy(quantity = item.quantity + 1))
        }
    }

    fun decrementQuantity(item: CartItem) {
        viewModelScope.launch {
            if (item.quantity > 1) {
                cartRepository.updateCartItem(item.copy(quantity = item.quantity - 1))
            } else {
                cartRepository.removeFromCart(item)
            }
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(item)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}