package com.example.tutedude.ecommerce.presentation.transaction

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
import com.example.tutedude.ecommerce.data.model.Transaction
import com.example.tutedude.ecommerce.ui.theme.*

/**
 * TransactionHistoryScreen - Display transaction history
 * File: TransactionHistoryScreen.kt
 * Purpose: Show all transactions with filters and search functionality
 */
@Composable
fun TransactionHistoryScreen(
    userId: String,
    onBackClick: () -> Unit,
    onTransactionDetailsClick: (Transaction) -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    LaunchedEffect(userId) {
        viewModel.loadUserTransactions(userId)
        viewModel.loadDailyStats(userId)
    }
    
    val transactions = viewModel.transactions.collectAsState()
    val dailyCount = viewModel.dailyTransactionCount.collectAsState()
    val dailySum = viewModel.dailyTransactionSum.collectAsState()
    val loading = viewModel.loading.collectAsState()
    
    var selectedFilter by remember { mutableStateOf("ALL") }
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredTransactions = remember(transactions.value, selectedFilter, searchQuery) {
        transactions.value.filter { transaction ->
            val matchesFilter = when (selectedFilter) {
                "SENT" -> transaction.type == "SENT"
                "RECEIVED" -> transaction.type == "RECEIVED"
                else -> true
            }
            val matchesSearch = searchQuery.isEmpty() || 
                transaction.recipientName.contains(searchQuery, ignoreCase = true) ||
                transaction.senderName.contains(searchQuery, ignoreCase = true) ||
                transaction.recipientUPI.contains(searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
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
                    "Transactions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // Daily Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    label = "Today's Transactions",
                    value = dailyCount.value.toString(),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "Today's Amount",
                    value = "₹${String.format("%.0f", dailySum.value)}",
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassLight.copy(alpha = 0.3f))
                    .border(
                        width = 1.dp,
                        color = NeonBlue.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text("Search by name or UPI...", color = TextGray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = NeonBlue
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = TextWhite,
                    focusedTextColor = TextWhite
                ),
                singleLine = true
            )
            
            // Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" },
                    label = { Text("All", color = if (selectedFilter == "ALL") TextWhite else TextGray) }
                )
                FilterChip(
                    selected = selectedFilter == "SENT",
                    onClick = { selectedFilter = "SENT" },
                    label = { Text("Sent", color = if (selectedFilter == "SENT") TextWhite else TextGray) }
                )
                FilterChip(
                    selected = selectedFilter == "RECEIVED",
                    onClick = { selectedFilter = "RECEIVED" },
                    label = { Text("Received", color = if (selectedFilter == "RECEIVED") TextWhite else TextGray) }
                )
            }
            
            // Transaction List
            if (filteredTransactions.isEmpty()) {
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
                            imageVector = Icons.Default.Receipt,
                            contentDescription = "No transactions",
                            tint = TextGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "No transactions yet",
                            fontSize = 18.sp,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            "Your transaction history will appear here",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredTransactions) { transaction ->
                        TransactionHistoryCard(
                            transaction = transaction,
                            onClick = { onTransactionDetailsClick(transaction) }
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
}

@Composable
fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(NeonBlue.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = NeonBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            label,
            fontSize = 11.sp,
            color = TextGray,
            textAlign = TextAlign.Center
        )
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = NeonBlue,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TransactionHistoryCard(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GlassLight.copy(alpha = 0.3f))
            .border(
                width = 1.dp,
                color = NeonBlue.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Transaction Icon & Info
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (transaction.type == "SENT") NeonRed.copy(alpha = 0.2f)
                        else NeonGreen.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (transaction.type == "SENT") Icons.Default.Send else Icons.Default.GetApp,
                    contentDescription = transaction.type,
                    tint = if (transaction.type == "SENT") NeonRed else NeonGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column {
                Text(
                    if (transaction.type == "SENT") "To: ${transaction.recipientName}" 
                    else "From: ${transaction.senderName}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    transaction.description.orEmpty(),
                    fontSize = 11.sp,
                    color = TextGray,
                    maxLines = 1
                )
                Text(
                    transaction.status,
                    fontSize = 10.sp,
                    color = when (transaction.status) {
                        "COMPLETED" -> NeonGreen
                        "FAILED" -> NeonRed
                        else -> NeonYellow
                    }
                )
            }
        }
        
        // Amount & Timestamp
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                if (transaction.type == "SENT") "-₹${String.format("%.2f", transaction.amount)}"
                else "+₹${String.format("%.2f", transaction.amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == "SENT") NeonRed else NeonGreen
            )
            Text(
                java.text.SimpleDateFormat("MM/dd HH:mm", java.util.Locale.getDefault())
                    .format(java.util.Date(transaction.initiatedAt)),
                fontSize = 10.sp,
                color = TextGray
            )
        }
    }
}
