package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.model.Product
import com.example.tutedude.ecommerce.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ProductRepository {

    override fun getAllProducts(): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = firestore.collection("products")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch products"))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(Resource.Success(products))
            }

        awaitClose { listenerRegistration.remove() }
    }

    override fun getProductById(productId: String): Flow<Resource<Product>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = firestore.collection("products")
            .document(productId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch product"))
                    return@addSnapshotListener
                }

                val product = snapshot?.toObject(Product::class.java)?.copy(id = snapshot.id)
                if (product != null) {
                    trySend(Resource.Success(product))
                } else {
                    trySend(Resource.Error("Product not found"))
                }
            }

        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun uploadProduct(product: Product): Resource<String> {
        return try {
            val docRef = firestore.collection("products").add(product).await()
            Resource.Success(docRef.id)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to upload product")
        }
    }

    override suspend fun deleteProduct(productId: String): Resource<Unit> {
        return try {
            firestore.collection("products").document(productId).delete().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to delete product")
        }
    }

    override fun searchProducts(query: String): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading())

        val listenerRegistration = firestore.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to search products"))
                    return@addSnapshotListener
                }

                val products = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Product::class.java)?.copy(id = doc.id)
                }?.filter { product ->
                    product.title.contains(query, ignoreCase = true) ||
                            product.description.contains(query, ignoreCase = true) ||
                            product.category.contains(query, ignoreCase = true)
                } ?: emptyList()

                trySend(Resource.Success(products))
            }

        awaitClose { listenerRegistration.remove() }
    }
}