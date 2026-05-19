package com.example.tutedude.ecommerce.data.repository

import com.example.tutedude.ecommerce.data.local.ApprovedMemberDao
import com.example.tutedude.ecommerce.data.model.ApprovedMember
import com.example.tutedude.ecommerce.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * ApprovedMemberRepository - Manages the 2-member parent approval system
 * File: ApprovedMemberRepository.kt
 * Purpose: Handle adding, approving, and managing family members for transaction approvals
 * 
 * Special Features:
 * - First member must be approved before adding second member
 * - Both members must approve for high-value transactions
 * - PIN verification required for member addition
 */
interface IApprovedMemberRepository {
    suspend fun addMember(member: ApprovedMember): Resource<Unit>
    suspend fun getMember(memberId: String): Resource<ApprovedMember>
    fun getMembersFlow(userId: String): Flow<List<ApprovedMember>>
    suspend fun getMemberByPosition(userId: String, position: Int): ApprovedMember?
    suspend fun updateMemberStatus(memberId: String, status: String): Resource<Unit>
    suspend fun approveMember(memberId: String): Resource<Unit>
    suspend fun rejectMember(memberId: String): Resource<Unit>
    suspend fun deleteMember(memberId: String): Resource<Unit>
    suspend fun getApprovedMemberCount(userId: String): Int
    suspend fun canAddSecondMember(userId: String): Boolean
    suspend fun getActiveApprovers(userId: String): List<ApprovedMember>
    suspend fun verifyMemberPin(memberId: String, pin: String): Boolean
}

class ApprovedMemberRepository @Inject constructor(
    private val approvedMemberDao: ApprovedMemberDao
) : IApprovedMemberRepository {
    
    override suspend fun addMember(member: ApprovedMember): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            approvedMemberDao.insertMember(member)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to add member"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getMember(memberId: String): Resource<ApprovedMember> = flow {
        try {
            emit(Resource.Loading())
            val member = approvedMemberDao.getMemberById(memberId)
            if (member != null) {
                emit(Resource.Success(member))
            } else {
                emit(Resource.Error("Member not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to fetch member"))
        }
    }.apply { collect { } as Unit }
    
    override fun getMembersFlow(userId: String): Flow<List<ApprovedMember>> {
        return approvedMemberDao.getMembersByUserFlow(userId)
    }
    
    override suspend fun getMemberByPosition(userId: String, position: Int): ApprovedMember? {
        return approvedMemberDao.getMemberByPosition(userId, position)
    }
    
    override suspend fun updateMemberStatus(memberId: String, status: String): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val date = if (status == "APPROVED") System.currentTimeMillis() else null
            approvedMemberDao.updateMemberStatus(memberId, status, date)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to update member status"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun approveMember(memberId: String): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val member = approvedMemberDao.getMemberById(memberId)
            if (member != null) {
                val updatedMember = member.copy(
                    status = "APPROVED",
                    approvalDate = System.currentTimeMillis(),
                    isVerified = true,
                    canApproveTransactions = true,
                    updatedAt = System.currentTimeMillis()
                )
                approvedMemberDao.updateMember(updatedMember)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Member not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to approve member"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun rejectMember(memberId: String): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val member = approvedMemberDao.getMemberById(memberId)
            if (member != null) {
                val updatedMember = member.copy(
                    status = "REJECTED",
                    rejectionDate = System.currentTimeMillis(),
                    canApproveTransactions = false,
                    updatedAt = System.currentTimeMillis()
                )
                approvedMemberDao.updateMember(updatedMember)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Member not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to reject member"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun deleteMember(memberId: String): Resource<Unit> = flow {
        try {
            emit(Resource.Loading())
            val member = approvedMemberDao.getMemberById(memberId)
            if (member != null) {
                approvedMemberDao.deleteMember(member)
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("Member not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete member"))
        }
    }.apply { collect { } as Unit }
    
    override suspend fun getApprovedMemberCount(userId: String): Int {
        return approvedMemberDao.getApprovedMemberCount(userId)
    }
    
    /**
     * Check if the first member is approved before allowing second member addition
     * This enforces the 2-member approval system rule
     */
    override suspend fun canAddSecondMember(userId: String): Boolean {
        val firstMember = approvedMemberDao.getMemberByPosition(userId, 1)
        return firstMember != null && firstMember.status == "APPROVED"
    }
    
    override suspend fun getActiveApprovers(userId: String): List<ApprovedMember> {
        return approvedMemberDao.getActiveApprovers(userId)
    }
    
    override suspend fun verifyMemberPin(memberId: String, pin: String): Boolean {
        // In a real implementation, this would hash the PIN and compare
        // For now, returning true (actual PIN verification should be implemented)
        val member = approvedMemberDao.getMemberById(memberId) ?: return false
        return member.isPinSet && !pin.isEmpty()
    }
}
