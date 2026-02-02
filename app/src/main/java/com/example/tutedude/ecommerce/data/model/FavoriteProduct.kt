package com.example.tutedude.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteProduct(
    @PrimaryKey
    val productId: String = "",
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val uploaderId: String = "",
    val uploaderName: String = "",
    val timestamp: Long = 0L
)