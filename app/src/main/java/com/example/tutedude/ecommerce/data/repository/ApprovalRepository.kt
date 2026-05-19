package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.local.ApprovalRequestDao
import com.example.tutedude.ecommerce.data.model.ApprovalRequest
import com.example.tutedude.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * ApprovalRepository - Manages approval requests for transactions
 * File: ApprovalRepository.kt
 * Purpose: Handle approval workflow, requests, and status tracking
 */
interface IApprovalRepository {
    suspend fun createApprovalRequest(request: ApprovalRequest): Resource<Unit>
    suspend fun getApprovalRequest(requestId: String): Resource<ApprovalRequest>
    fun getApprovalsByTransactionFlow(transactionId: String): Flow<List<ApprovalRequest>>
    fun getApprovalsByUserFlow(userId: String): Flow<List<ApprovalRequest>>
    fun getPendingApprovalsForMemberFlow(memberId: String): Flow<List<ApprovalRequest>>
    suspend fun approveRequest(requestId: String, approverId: String): Resource<Unit>
    suspend fun rejectRequest(requestId: String, approverId: String, reason: String?): Resource<Unit>
    suspend fun updateRequest(request: ApprovalRequest): Resource<Unit>
    suspend fun getPendingApprovalCount(userId: String): Int
    suspend fun getPendingApprovalsForTransaction(transactionId: String): List<ApprovalRequest>
    suspend fun verifyAndApprove(requestId: String, pin: String): Resource<Boolean>
    suspend fun verifyOtp(requestId: String, otp: String): Resource<Boolean>
}

class ApprovalRepository @Inject constructor(
    private val approvalRequestDao: ApprovalRequestDao
) : IApprovalRepository {
    
    override suspend fun createApprovalRequest(request: ApprovalRequest): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            approvalRequestDao.insertApprovalRequest(request)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to create approval request"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getApprovalRequest(requestId: String): Resource<ApprovalRequest> = flow {
        try {
            emit(Resource.Loading())
            val request = approvalRequestDao.getApprovalRequestById(requestId)
            if (request != null) {
                emit(Resource.Success(request))
            } else {
                emit(Resource.Error("Approval request not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch approval request"))
        }
    }.apply { collect { } as Unit }
    
    override fun getApprovalsByTransactionFlow(transactionId: String): Flow<List<ApprovalRequest>> {
        return approvalRequestDao.getApprovalsByTransactionFlow(transactionId)
    }
    
    override fun getApprovalsByUserFlow(userId: String): Flow<List<ApprovalRequest>> {
        return approvalRequestDao.getApprovalsByUserFlow(userId)
    }
    
    override fun getPendingApprovalsForMemberFlow(memberId: String): Flow<List<ApprovalRequest>> {
        return approvalRequestDao.getPendingApprovalsForMemberFlow(memberId)
    }
    
    override suspend fun approveRequest(requestId: String, approverId: String): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val request = approvalRequestDao.getApprovalRequestById(requestId)
            if (request != null && request.status == "PENDING") {
                approvalRequestDao.approveRequest(requestId, "APPROVED", System.currentTimeMillis())
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Cannot approve this request"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to approve request"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun rejectRequest(requestId: String, approverId: String, reason: String?): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val request = approvalRequestDao.getApprovalRequestById(requestId)
            if (request != null && request.status == "PENDING") {
                val updatedRequest = request.copy(
                    status = "REJECTED",
                    rejectedAt = System.currentTimeMillis(),
                    approvalResponse = reason,
                    updatedAt = System.currentTimeMillis()
                )
                approvalRequestDao.updateApprovalRequest(updatedRequest)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Cannot reject this request"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to reject request"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun updateRequest(request: ApprovalRequest): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            approvalRequestDao.updateApprovalRequest(request)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update request"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getPendingApprovalCount(userId: String): Int {
        return approvalRequestDao.getPendingApprovalCount(userId)
    }
    
    override suspend fun getPendingApprovalsForTransaction(transactionId: String): List<ApprovalRequest> {
        return approvalRequestDao.getPendingApprovalsForTransaction(transactionId)
    }
    
    override suspend fun verifyAndApprove(requestId: String, pin: String): Resource<Boolean> = flow {
        try {
            emit(Resource.Loading())
            val request = approvalRequestDao.getApprovalRequestById(requestId)
            if (request != null && request.pinRequired) {
                // In real app, verify PIN hash
                // For now, assuming PIN is correct if provided
                if (pin.isNotEmpty()) {
                    val updatedRequest = request.copy(
                        pinVerified = true,
                        status = "APPROVED",
                        approvedAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    approvalRequestDao.updateApprovalRequest(updatedRequest)
                    emit(Resource.Success(true))
                } else {
                    emit(Resource.Error("Invalid PIN"))
                }
            } else {
                emit(Resource.Error("PIN not required or request not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to verify and approve"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun verifyOtp(requestId: String, otp: String): Resource<Boolean> = flow {
        try {
            emit(Resource.Loading())
            val request = approvalRequestDao.getApprovalRequestById(requestId)
            if (request != null && request.otpRequired) {
                if (otp.isNotEmpty() && otp.length == 6) {
                    val updatedRequest = request.copy(
                        otpVerified = true,
                        updatedAt = System.currentTimeMillis()
                    )
                    approvalRequestDao.updateApprovalRequest(updatedRequest)
                    emit(Resource.Success(true))
                } else {
                    emit(Resource.Error("Invalid OTP"))
                }
            } else {
                emit(Resource.Error("OTP not required or request not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to verify OTP"))
        }
    }.apply { collect { } as Unit }
}
