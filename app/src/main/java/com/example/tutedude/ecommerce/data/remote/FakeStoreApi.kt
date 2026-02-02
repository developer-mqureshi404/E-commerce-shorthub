package com.example.tutedude.ecommerce.data.remote

import com.example.tutedude.ecommerce.data.model.RecommendedProduct
import retrofit2.http.GET

interface FakeStoreApi {

    @GET("products")
    suspend fun getProducts(): List<RecommendedProduct>

    @GET("products/categories")
    suspend fun getCategories(): List<String>
}