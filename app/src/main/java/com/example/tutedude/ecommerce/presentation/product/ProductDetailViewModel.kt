package com.example.tutedude.ecommerce.presentation.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.Product
import com.example.tutedude.ecommerce.data.repository.ProductRepository
import com.example.tutedude.ecommerce.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProduct()
    }

    private fun loadProduct() {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = ProductDetailUiState.Loading
                    }
                    is Resource.Success -> {
                        if (result.data != null) {
                            _uiState.value = ProductDetailUiState.Success(result.data)
                        } else {
                            _uiState.value = ProductDetailUiState.Error("Product not found")
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = ProductDetailUiState.Error(
                            result.message ?: "Failed to load product"
                        )
                    }
                }
            }
        }
    }

    fun retry() {
        loadProduct()
    }
}

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
}