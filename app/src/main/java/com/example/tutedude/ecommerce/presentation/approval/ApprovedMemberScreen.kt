package com.example.tutedude.ecommerce.presentation.approval

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutedude.ecommerce.data.model.ApprovedMember
import com.example.tutedude.ecommerce.ui.theme.*

/**
 * ApprovedMemberScreen - Manages the 2-member family approval system
 * File: ApprovedMemberScreen.kt
 * Purpose: Handle adding, approving, and managing family members for transaction approvals
 * 
 * Critical Feature: Enforces 2-member approval system where:
 * - First member must be approved before adding second member
 * - Both members must approve for high-value transactions
 * - PIN verification required for member approval
 */
@Composable
fun ApprovedMemberScreen(
    userId: String,
    onBackClick: () -> Unit,
    viewModel: ApprovalViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) {
        viewModel.loadApprovedMembers(userId)
        viewModel.loadPendingApprovals(userId)
    }
    
    val approvedMembers = viewModel.approvedMembers.collectAsState()
    val isFirstMemberApproved = viewModel.isFirstMemberApproved.collectAsState()
    val approvedMemberCount = viewModel.approvedMemberCount.collectAsState()
    val canAddSecondMember = viewModel.canAddSecondMember.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val error = viewModel.error.collectAsState()
    val successMessage = viewModel.successMessage.collectAsState()
    
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf<ApprovedMember?>(null) }
    var showApprovalDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(error.value) {
        if (error.value != null) {
            // Show error snackbar
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GlassDark, GlassLight)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = NeonBlue
                    )
                }
                Text(
                    "Family Approvers",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // Info Card: 2-Member Approval System
            InfoCard(
                icon = Icons.Default.Info,
                title = "2-Member Approval System",
                description = "You need approval from 2 family members for high-value transactions",
                color = NeonBlue
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Member Status Overview
            MemberStatusOverview(
                memberCount = approvedMemberCount.value,
                isFirstApproved = isFirstMemberApproved.value,
                canAddSecond = canAddSecondMember.value
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Added Members Section
            Text(
                "Added Members",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            if (approvedMembers.value.isEmpty()) {
                EmptyStateCard(
                    icon = Icons.Default.PersonAdd,
                    message = "No family members added yet",
                    subMessage = "Add 2 family members/parents to secure your account"
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(approvedMembers.value) { member ->
                        ApprovedMemberCard(
                            member = member,
                            onMemberClick = {
                                selectedMember = it
                                showApprovalDialog = true
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Add Member Button
            if (approvedMemberCount.value < 2) {
                Button(
                    onClick = { showAddMemberDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonBlue
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = if (approvedMemberCount.value == 1) canAddSecondMember.value else true
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (approvedMemberCount.value == 0) "Add First Member" else "Add Second Member",
                        fontSize = 16.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Warning if first member not approved
            if (approvedMemberCount.value == 1 && !isFirstMemberApproved.value) {
                Spacer(modifier = Modifier.height(16.dp))
                WarningCard(
                    message = "First member approval pending. Please get approval before adding the second member.",
                    color = NeonOrange
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Loading Indicator
        if (loading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = NeonBlue
            )
        }
    }
    
    // Add Member Dialog
    if (showAddMemberDialog) {
        AddMemberDialog(
            onDismiss = { showAddMemberDialog = false },
            onAddMember = { name, phone, email, relationship ->
                // Handle add member
                showAddMemberDialog = false
            },
            isFirstMember = approvedMemberCount.value == 0
        )
    }
    
    // Approval Dialog
    if (showApprovalDialog && selectedMember != null) {
        MemberApprovalDialog(
            member = selectedMember!!,
            onDismiss = { showApprovalDialog = false },
            onApprove = { pin ->
                viewModel.approveRequest("", pin)  // requestId should come from selectedMember
                showApprovalDialog = false
            },
            onReject = { reason ->
                viewModel.rejectRequest("", reason)  // requestId should come from selectedMember
                showApprovalDialog = false
            }
        )
    }
}

@Composable
fun MemberStatusOverview(
    memberCount: Int,
    isFirstApproved: Boolean,
    canAddSecond: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(NeonBlue.copy(alpha = 0.1f), NeonPurple.copy(alpha = 0.1f))
                )
            )
            .border(
                width = 1.dp,
                color = NeonBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$memberCount",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue
            )
            Text(
                "Members Added",
                fontSize = 12.sp,
                color = TextGray
            )
        }
        
        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(60.dp),
            color = NeonBlue.copy(alpha = 0.2f)
        )
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (isFirstApproved) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = "First Member Status",
                tint = if (isFirstApproved) NeonGreen else NeonRed,
                modifier = Modifier.size(24.dp)
            )
            Text(
                if (isFirstApproved) "1st Approved" else "1st Pending",
                fontSize = 12.sp,
                color = TextGray
            )
        }
        
        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(60.dp),
            color = NeonBlue.copy(alpha = 0.2f)
        )
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = if (canAddSecond) Icons.Default.CheckCircle else Icons.Default.Lock,
                contentDescription = "Second Member Status",
                tint = if (canAddSecond) NeonGreen else NeonRed,
                modifier = Modifier.size(24.dp)
            )
            Text(
                "2nd Ready",
                fontSize = 12.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun ApprovedMemberCard(
    member: ApprovedMember,
    onMemberClick: (ApprovedMember) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassLight.copy(alpha = 0.3f))
            .clickable { onMemberClick(member) }
            .border(
                width = 1.dp,
                color = when (member.status) {
                    "APPROVED" -> NeonGreen.copy(alpha = 0.3f)
                    "REJECTED" -> NeonRed.copy(alpha = 0.3f)
                    else -> NeonYellow.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    "${member.memberPosition}. ${member.memberName}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(member.status)
            }
            Text(
                member.relationship,
                fontSize = 12.sp,
                color = TextGray
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open",
            tint = NeonBlue
        )
    }
}

@Composable
fun StatusBadge(status: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                when (status) {
                    "APPROVED" -> NeonGreen.copy(alpha = 0.2f)
                    "REJECTED" -> NeonRed.copy(alpha = 0.2f)
                    else -> NeonYellow.copy(alpha = 0.2f)
                }
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            status,
            fontSize = 10.sp,
            color = when (status) {
                "APPROVED" -> NeonGreen
                "REJECTED" -> NeonRed
                else -> NeonYellow
            },
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoCard(
    icon: androidx.compose.material.icons.Icons,
    title: String,
    description: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                description,
                fontSize = 12.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: androidx.compose.material.icons.Icons,
    message: String,
    subMessage: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassLight.copy(alpha = 0.3f))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = message,
            tint = TextGray,
            modifier = Modifier
                .size(48.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            message,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextWhite,
            textAlign = TextAlign.Center
        )
        Text(
            subMessage,
            fontSize = 12.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun WarningCard(
    message: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = color.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Warning",
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            message,
            fontSize = 12.sp,
            color = TextWhite
        )
    }
}

@Composable
fun AddMemberDialog(
    onDismiss: () -> Unit,
    onAddMember: (String, String, String, String) -> Unit,
    isFirstMember: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (isFirstMember) "Add First Member" else "Add Second Member",
                color = TextWhite
            )
        },
        text = {
            Text("Dialog implementation for adding member")
        },
        confirmButton = {
            Button(onClick = { onAddMember("", "", "", "") }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun MemberApprovalDialog(
    member: ApprovedMember,
    onDismiss: () -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Approve Member", color = TextWhite)
        },
        text = {
            Text("Dialog for member approval with PIN")
        },
        confirmButton = {
            Button(onClick = { onApprove("") }) {
                Text("Approve")
            }
        },
        dismissButton = {
            Button(onClick = { onReject("Not approved") }) {
                Text("Reject")
            }
        }
    )
}
