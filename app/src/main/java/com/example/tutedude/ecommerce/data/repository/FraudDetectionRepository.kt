package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.local.FraudAlertDao
import com.example.tutedude.ecommerce.data.model.FraudAlert
import com.example.tutedude.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * FraudDetectionRepository - Manages fraud detection and alerts
 * File: FraudDetectionRepository.kt
 * Purpose: Handle fraud detection, alerting, and transaction blocking
 */
interface IFraudDetectionRepository {
    suspend fun createAlert(alert: FraudAlert): Resource<Unit>
    suspend fun getAlert(alertId: String): Resource<FraudAlert>
    fun getAlertsByUserFlow(userId: String): Flow<List<FraudAlert>>
    fun getPendingAlertsFlow(userId: String): Flow<List<FraudAlert>>
    suspend fun updateAlert(alert: FraudAlert): Resource<Unit>
    suspend fun deleteAlert(alert: FraudAlert): Resource<Unit>
    suspend fun getPendingAlertCount(userId: String): Int
    suspend fun analyzeFraudScore(userId: String, amount: Double, location: String?): Float
    suspend fun shouldBlockTransaction(userId: String, fraudScore: Float): Boolean
}

class FraudDetectionRepository @Inject constructor(
    private val fraudAlertDao: FraudAlertDao
) : IFraudDetectionRepository {
    
    override suspend fun createAlert(alert: FraudAlert): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            fraudAlertDao.insertAlert(alert)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to create fraud alert"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getAlert(alertId: String): Resource<FraudAlert> = flow {
        try {
            emit(Resource.Loading())
            val alert = fraudAlertDao.getAlertById(alertId)
            if (alert != null) {
                emit(Resource.Success(alert))
            } else {
                emit(Resource.Error("Alert not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch alert"))
        }
    }.apply { collect { } as Unit }
    
    override fun getAlertsByUserFlow(userId: String): Flow<List<FraudAlert>> {
        return fraudAlertDao.getAlertsByUserFlow(userId)
    }
    
    override fun getPendingAlertsFlow(userId: String): Flow<List<FraudAlert>> {
        return fraudAlertDao.getPendingAlertsFlow(userId)
    }
    
    override suspend fun updateAlert(alert: FraudAlert): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            fraudAlertDao.updateAlert(alert)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update alert"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun deleteAlert(alert: FraudAlert): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            fraudAlertDao.deleteAlert(alert)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete alert"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getPendingAlertCount(userId: String): Int {
        return fraudAlertDao.getPendingAlertCount(userId)
    }
    
    /**
     * Analyze transaction for fraud patterns
     * Returns a score from 0-100, where higher = more suspicious
     */
    override suspend fun analyzeFraudScore(userId: String, amount: Double, location: String?): Float {
        var fraudScore = 0f
        
        // Check unusual amount (simplified logic)
        if (amount > 50000) {
            fraudScore += 30f  // High amount increases suspicion
        } else if (amount > 100000) {
            fraudScore += 50f  // Very high amount
        }
        
        // Check unusual time patterns (simplified - should use ML in real app)
        val currentHour = java.time.LocalDateTime.now().hour
        if (currentHour >= 2 && currentHour <= 5) {
            fraudScore += 20f  // Unusual time
        }
        
        // Location-based check (simplified)
        // In real app, would check against historical locations
        if (location != null && location.isNotEmpty()) {
            fraudScore += 10f  // Location change increases suspicion slightly
        }
        
        return minOf(fraudScore, 100f)
    }
    
    /**
     * Determine if transaction should be blocked based on fraud score
     */
    override suspend fun shouldBlockTransaction(userId: String, fraudScore: Float): Boolean {
        // Block if score is critical (> 80)
        return fraudScore > 80f
    }
}
