package com.example.tutedude.ecommerce.presentation.upload

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.Product
import com.example.tutedude.ecommerce.data.repository.AuthRepository
import com.example.tutedude.ecommerce.data.repository.ProductRepository
import com.example.tutedude.ecommerce.util.Resource
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UploadProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository,
    private val storage: FirebaseStorage
) : ViewModel() {

    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState: StateFlow<UploadState> = _uploadState.asStateFlow()

    fun uploadProduct(
        title: String,
        description: String,
        price: Double,
        category: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading

            try {
                val currentUser = authRepository.getCurrentUser()
                if (currentUser == null) {
                    _uploadState.value = UploadState.Error("User not logged in")
                    return@launch
                }

                val imageUrl = if (imageUri != null) {
                    uploadImage(imageUri)
                } else {
                    ""
                }

                val product = Product(
                    title = title,
                    description = description,
                    price = price,
                    category = category,
                    imageUrl = imageUrl,
                    uploaderId = currentUser.uid,
                    uploaderName = currentUser.displayName,
                    uploaderEmail = currentUser.email,
                    timestamp = System.currentTimeMillis()
                )

                when (val result = productRepository.uploadProduct(product)) {
                    is Resource.Success -> {
                        _uploadState.value = UploadState.Success
                    }
                    is Resource.Error -> {
                        _uploadState.value = UploadState.Error(
                            result.message ?: "Failed to upload product"
                        )
                    }
                    is Resource.Loading -> {}
                }
            } catch (e: Exception) {
                _uploadState.value = UploadState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private suspend fun uploadImage(uri: Uri): String {
        val filename = "products/${System.currentTimeMillis()}.jpg"
        val ref = storage.reference.child(filename)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    fun resetState() {
        _uploadState.value = UploadState.Idle
    }
}

sealed class UploadState {
    object Idle : UploadState()
    object Loading : UploadState()
    object Success : UploadState()
    data class Error(val message: String) : UploadState()
}