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
import com.example.tutedude.ecommerce.data.model.ApprovalRequest
import com.example.tutedude.ecommerce.ui.theme.*

/**
 * ApprovalNotificationScreen - Display pending approvals and approval history
 * File: ApprovalNotificationScreen.kt
 * Purpose: Show approval requests with PIN verification and action buttons
 */
@Composable
fun ApprovalNotificationScreen(
    userId: String,
    memberId: String,
    onBackClick: () -> Unit,
    viewModel: ApprovalViewModel = hiltViewModel()
) {
    LaunchedEffect(memberId) {
        viewModel.loadPendingApprovalsForMember(memberId)
    }
    
    val pendingApprovals = viewModel.pendingApprovals.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val error = viewModel.error.collectAsState()
    val successMessage = viewModel.successMessage.collectAsState()
    
    var selectedApproval by remember { mutableStateOf<ApprovalRequest?>(null) }
    var showApprovalDialog by remember { mutableStateOf(false) }
    var showPINDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(error.value) {
        if (error.value != null) {
            // Show error snackbar
        }
    }
    
    LaunchedEffect(successMessage.value) {
        if (successMessage.value != null) {
            // Show success snackbar
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
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                    "Approvals",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            if (pendingApprovals.value.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "All approved",
                            tint = NeonGreen,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "All Clear!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            "No pending approvals",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    }
                }
            } else {
                // Pending Approvals List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(pendingApprovals.value) { approval ->
                        ApprovalRequestCard(
                            approval = approval,
                            onApprove = {
                                selectedApproval = approval
                                showPINDialog = true
                            },
                            onReject = {
                                selectedApproval = approval
                                showApprovalDialog = true
                            }
                        )
                    }
                }
            }
        }
        
        if (loading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = NeonBlue
            )
        }
    }
    
    // PIN Verification Dialog
    if (showPINDialog && selectedApproval != null) {
        PINVerificationDialog(
            onDismiss = { showPINDialog = false },
            onVerify = { pin ->
                viewModel.approveRequest(selectedApproval!!.requestId, pin)
                showPINDialog = false
            }
        )
    }
    
    // Rejection Dialog
    if (showApprovalDialog && selectedApproval != null) {
        RejectApprovalDialog(
            amount = selectedApproval!!.amount,
            onDismiss = { showApprovalDialog = false },
            onReject = { reason ->
                viewModel.rejectRequest(selectedApproval!!.requestId, reason)
                showApprovalDialog = false
            }
        )
    }
}

@Composable
fun ApprovalRequestCard(
    approval: ApprovalRequest,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonYellow.copy(alpha = 0.1f),
                        NeonOrange.copy(alpha = 0.1f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = NeonYellow.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonYellow.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Pending",
                        tint = NeonYellow,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Column {
                    Text(
                        "Payment Request",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        "Pending your approval",
                        fontSize = 11.sp,
                        color = TextGray
                    )
                }
            }
            
            StatusBadge("PENDING")
        }
        
        Divider(
            color = NeonYellow.copy(alpha = 0.2f),
            modifier = Modifier.padding(vertical = 12.dp)
        )
        
        // Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Amount",
                    fontSize = 11.sp,
                    color = TextGray
                )
                Text(
                    "₹${String.format("%.2f", approval.amount)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "To: ${approval.recipientName}",
                    fontSize = 11.sp,
                    color = TextGray
                )
                Text(
                    approval.recipientUPI,
                    fontSize = 12.sp,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        if (approval.description != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Note: ${approval.description}",
                    fontSize = 11.sp,
                    color = TextGray
                )
            }
        }
        
        Divider(
            color = NeonYellow.copy(alpha = 0.2f),
            modifier = Modifier.padding(vertical = 12.dp)
        )
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onReject,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonRed.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, NeonRed)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Reject",
                    tint = NeonRed,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Reject",
                    fontSize = 12.sp,
                    color = NeonRed,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Button(
                onClick = onApprove,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Approve",
                    tint = TextWhite,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Approve",
                    fontSize = 12.sp,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PINVerificationDialog(
    onDismiss: () -> Unit,
    onVerify: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var showPIN by remember { mutableStateOf(false) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Verify PIN", color = TextWhite, fontSize = 20.sp)
        },
        text = {
            Column {
                Text(
                    "Enter your 4-digit PIN to approve this transaction",
                    color = TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    placeholder = {
                        Text("••••", color = TextGray)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = TextWhite,
                        focusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onVerify(pin) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                enabled = pin.length == 4
            ) {
                Text("Verify")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonRed.copy(alpha = 0.2f)
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RejectApprovalDialog(
    amount: Double,
    onDismiss: () -> Unit,
    onReject: (String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Reject Approval", color = TextWhite, fontSize = 20.sp)
        },
        text = {
            Column {
                Text(
                    "Are you sure you want to reject ₹${String.format("%.2f", amount)}?",
                    color = TextGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = reason,
                    onValueChange = { reason = it },
                    placeholder = {
                        Text("Reason (optional)", color = TextGray)
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedTextColor = TextWhite,
                        focusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onReject(reason) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
            ) {
                Text("Reject")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonBlue.copy(alpha = 0.2f)
                )
            ) {
                Text("Cancel")
            }
        }
    )
}
