package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.model.Product
import com.example.tutedude.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<Resource<List<Product>>>
    fun getProductById(productId: String): Flow<Resource<Product>>
    suspend fun uploadProduct(product: Product): Resource<String>
    suspend fun deleteProduct(productId: String): Resource<Unit>
    fun searchProducts(query: String): Flow<Resource<List<Product>>>
}