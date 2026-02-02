package com.example.tutedude.ecommerce.data.model

data class Product(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val uploaderId: String = "",
    val uploaderName: String = "",
    val uploaderEmail: String = "",
    val timestamp: Long = System.currentTimeMillis()
)