package com.example.tutedude.ecommerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.Product
import com.example.tutedude.ecommerce.data.repository.AuthRepository
import com.example.tutedude.ecommerce.data.repository.ProductRepository
import com.example.tutedude.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productsState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductsState.Loading
                    }
                    is Resource.Success -> {
                        _productsState.value = ProductsState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _productsState.value = ProductsState.Error(
                            resource.message ?: "Failed to load products"
                        )
                    }
                }
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            productRepository.searchProducts(query).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _productsState.value = ProductsState.Loading
                    }
                    is Resource.Success -> {
                        _productsState.value = ProductsState.Success(resource.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _productsState.value = ProductsState.Error(
                            resource.message ?: "Failed to search products"
                        )
                    }
                }
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}

sealed class ProductsState {
    object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data class Error(val message: String) : ProductsState()
}