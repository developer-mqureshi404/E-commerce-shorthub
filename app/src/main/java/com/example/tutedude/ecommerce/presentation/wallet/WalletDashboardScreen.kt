package com.example.tutedude.ecommerce.presentation.wallet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.tutedude.ecommerce.ui.theme.*

/**
 * WalletDashboardScreen - Main wallet dashboard with glassmorphism design
 * File: WalletDashboardScreen.kt
 * Purpose: Display balance, quick actions, and recent transactions with modern UI
 */
@Composable
fun WalletDashboardScreen(
    userId: String,
    onSendMoneyClick: () -> Unit,
    onReceiveMoneyClick: () -> Unit,
    onQRScanClick: () -> Unit,
    onTransactionHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: WalletViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) {
        viewModel.initWallet(userId)
    }
    
    val balance = viewModel.balance.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val error = viewModel.error.collectAsState()
    
    if (error.value != null) {
        LaunchedEffect(error.value) {
            // Handle error display
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(GlassDark, GlassLight),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
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
                Column {
                    Text(
                        "Welcome back!",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                    Text(
                        "GnardeWallet",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = NeonBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            // Balance Card with Glassmorphism
            GlassmorphismCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Balance",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                    
                    Text(
                        "₹${String.format("%.2f", balance.value)}",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "UPI Active",
                            fontSize = 12.sp,
                            color = NeonGreen
                        )
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified",
                            tint = NeonGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            // Quick Actions
            Text(
                "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.Send,
                    label = "Send",
                    onClick = onSendMoneyClick,
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    icon = Icons.Default.GetApp,
                    label = "Receive",
                    onClick = onReceiveMoneyClick,
                    modifier = Modifier.weight(1f)
                )
                QuickActionButton(
                    icon = Icons.Default.QrCode,
                    label = "Scan QR",
                    onClick = onQRScanClick,
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Recent Transactions Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Transactions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    "View All",
                    fontSize = 12.sp,
                    color = NeonBlue,
                    modifier = Modifier.clickable(onClick = onTransactionHistoryClick)
                )
            }
            
            // Transaction List Placeholder
            repeat(3) {
                TransactionListItem(
                    transactionType = if (it % 2 == 0) "Sent" else "Received",
                    amount = 5000.0 + (it * 1000),
                    recipient = "John Doe",
                    timestamp = "2 hours ago"
                )
            }
        }
        
        // Loading Indicator
        if (loading.value) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = NeonBlue
            )
        }
    }
}

@Composable
fun GlassmorphismCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1A1E3F).copy(alpha = 0.6f),
                        Color(0xFF2A2E4F).copy(alpha = 0.4f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = NeonBlue.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        content()
    }
}

@Composable
fun QuickActionButton(
    icon: androidx.compose.material.icons.Icons,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(NeonBlue.copy(alpha = 0.1f), NeonPurple.copy(alpha = 0.1f))
                )
            )
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = NeonBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = NeonBlue,
            modifier = Modifier
                .size(28.dp)
                .padding(bottom = 8.dp)
        )
        Text(
            label,
            fontSize = 12.sp,
            color = TextWhite,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TransactionListItem(
    transactionType: String,
    amount: Double,
    recipient: String,
    timestamp: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(GlassLight.copy(alpha = 0.3f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                recipient,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                timestamp,
                fontSize = 12.sp,
                color = TextGray
            )
        }
        
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                if (transactionType == "Sent") "-₹${String.format("%.2f", amount)}" else "+₹${String.format("%.2f", amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (transactionType == "Sent") NeonRed else NeonGreen
            )
            Text(
                transactionType,
                fontSize = 11.sp,
                color = TextGray
            )
        }
    }
}
