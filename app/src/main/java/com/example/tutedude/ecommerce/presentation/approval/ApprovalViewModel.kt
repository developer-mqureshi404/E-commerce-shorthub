package com.example.tutedude.ecommerce.presentation.approval

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tutedude.ecommerce.data.model.ApprovalRequest
import com.example.tutedude.ecommerce.data.model.ApprovedMember
import com.example.tutedude.ecommerce.data.repository.IApprovalRepository
import com.example.tutedude.ecommerce.data.repository.IApprovedMemberRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ApprovalViewModel - Manages the 2-member family approval system
 * File: ApprovalViewModel.kt
 * Purpose: Handle approval requests, member management, and approval workflow
 */
@HiltViewModel
class ApprovalViewModel @Inject constructor(
    private val approvalRepository: IApprovalRepository,
    private val approvedMemberRepository: IApprovedMemberRepository
) : ViewModel() {
    
    // Approval Requests
    private val _pendingApprovals = MutableStateFlow<List<ApprovalRequest>>(emptyList())
    val pendingApprovals: StateFlow<List<ApprovalRequest>> = _pendingApprovals.asStateFlow()
    
    // Approved Members (max 2)
    private val _approvedMembers = MutableStateFlow<List<ApprovedMember>>(emptyList())
    val approvedMembers: StateFlow<List<ApprovedMember>> = _approvedMembers.asStateFlow()
    
    // UI State
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()
    
    // First Member Status (for 2-member approval system)
    private val _isFirstMemberApproved = MutableStateFlow(false)
    val isFirstMemberApproved: StateFlow<Boolean> = _isFirstMemberApproved.asStateFlow()
    
    private val _approvedMemberCount = MutableStateFlow(0)
    val approvedMemberCount: StateFlow<Int> = _approvedMemberCount.asStateFlow()
    
    private val _canAddSecondMember = MutableStateFlow(false)
    val canAddSecondMember: StateFlow<Boolean> = _canAddSecondMember.asStateFlow()
    
    private val _selectedApprovalRequest = MutableStateFlow<ApprovalRequest?>(null)
    val selectedApprovalRequest: StateFlow<ApprovalRequest?> = _selectedApprovalRequest.asStateFlow()
    
    /**
     * Load approved members for the user
     */
    fun loadApprovedMembers(userId: String) {
        viewModelScope.launch {
            try {
                approvedMemberRepository.getMembersFlow(userId).collect { members ->
                    _approvedMembers.value = members
                    
                    // Check if first member is approved
                    val firstMember = members.find { it.memberPosition == 1 }
                    _isFirstMemberApproved.value = firstMember?.status == "APPROVED"
                    
                    // Count approved members
                    _approvedMemberCount.value = approvedMemberRepository.getApprovedMemberCount(userId)
                    
                    // Check if second member can be added
                    _canAddSecondMember.value = approvedMemberRepository.canAddSecondMember(userId)
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    /**
     * Load pending approvals for the user
     */
    fun loadPendingApprovals(userId: String) {
        viewModelScope.launch {
            try {
                approvalRepository.getApprovalsByUserFlow(userId).collect { approvals ->
                    _pendingApprovals.value = approvals.filter { it.status == "PENDING" }
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    /**
     * Load pending approvals for a specific family member
     */
    fun loadPendingApprovalsForMember(memberId: String) {
        viewModelScope.launch {
            try {
                approvalRepository.getPendingApprovalsForMemberFlow(memberId).collect { approvals ->
                    _pendingApprovals.value = approvals
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    
    /**
     * Approve an approval request with PIN verification
     */
    fun approveRequest(requestId: String, pin: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                approvalRepository.verifyAndApprove(requestId, pin)
                _successMessage.value = "Transaction approved successfully"
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
    
    /**
     * Reject an approval request
     */
    fun rejectRequest(requestId: String, reason: String? = null) {
        viewModelScope.launch {
            _loading.value = true
            try {
                // Assuming approverMemberId is retrieved from context
                approvalRepository.rejectRequest(requestId, "approver", reason)
                _successMessage.value = "Transaction rejected"
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }
    
    /**
     * Select approval request for details view
     */
    fun selectApprovalRequest(request: ApprovalRequest) {
        _selectedApprovalRequest.value = request
    }
    
    fun clearSelection() {
        _selectedApprovalRequest.value = null
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun clearSuccess() {
        _successMessage.value = null
    }
}
