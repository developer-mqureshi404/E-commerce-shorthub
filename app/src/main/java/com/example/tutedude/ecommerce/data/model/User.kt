package com.example.tutedude.ecommerce.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)