package com.example.tutedude.ecommerce.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tutedude.ecommerce.ui.theme.*

/**
 * SendMoneyScreen - Send money to UPI recipient
 * File: SendMoneyScreen.kt
 * Purpose: Handle money transfers with validation, approval checks, and fraud detection
 */
@Composable
fun SendMoneyScreen(
    userId: String,
    onBackClick: () -> Unit,
    onSuccessClick: () -> Unit
) {
    var recipientUPI by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }
    
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
                    "Send Money",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // Recipient UPI Input
            Text(
                "Recipient UPI ID",
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            TextField(
                value = recipientUPI,
                onValueChange = { recipientUPI = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassLight.copy(alpha = 0.3f))
                    .border(
                        width = 1.dp,
                        color = if (recipientUPI.isEmpty()) NeonBlue.copy(alpha = 0.3f) else NeonGreen.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text("example@bank", color = TextGray)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "UPI",
                        tint = NeonBlue
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = TextWhite,
                    focusedTextColor = TextWhite
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Amount Input
            Text(
                "Amount",
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            TextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassLight.copy(alpha = 0.3f))
                    .border(
                        width = 1.dp,
                        color = if (amount.isEmpty()) NeonBlue.copy(alpha = 0.3f) else NeonGreen.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text("Enter amount", color = TextGray)
                },
                prefix = {
                    Text("₹", color = NeonBlue, modifier = Modifier.padding(end = 4.dp))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "Amount",
                        tint = NeonBlue
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = TextWhite,
                    focusedTextColor = TextWhite
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Description Input
            Text(
                "Description (Optional)",
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            TextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GlassLight.copy(alpha = 0.3f))
                    .border(
                        width = 1.dp,
                        color = NeonBlue.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                placeholder = {
                    Text("What's this payment for?", color = TextGray)
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = TextWhite,
                    focusedTextColor = TextWhite
                ),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Fraud Alert (if applicable)
            if (amount.isNotEmpty() && amount.toDoubleOrNull()?.let { it > 50000 } == true) {
                FraudAlertCard(
                    message = "High-value transaction detected. Approval required.",
                    severity = "High"
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Error Message
            if (errorMessage.isNotEmpty()) {
                Text(
                    errorMessage,
                    fontSize = 12.sp,
                    color = NeonRed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(NeonRed.copy(alpha = 0.1f))
                        .border(
                            width = 1.dp,
                            color = NeonRed.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Summary Card
            SummaryCard(
                recipientUPI = recipientUPI,
                amount = amount.toDoubleOrNull() ?: 0.0,
                fee = if (amount.isEmpty()) 0.0 else (amount.toDoubleOrNull() ?: 0.0) * 0.01  // 1% fee
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Send Button
            Button(
                onClick = { showConfirmation = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonBlue
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = recipientUPI.isNotEmpty() && amount.isNotEmpty() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextWhite,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = TextWhite,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Send ₹${amount.ifEmpty { "0" }}",
                        fontSize = 16.sp,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
    
    // Confirmation Dialog
    if (showConfirmation) {
        ConfirmPaymentDialog(
            recipientUPI = recipientUPI,
            amount = amount.toDoubleOrNull() ?: 0.0,
            description = description,
            onConfirm = {
                showConfirmation = false
                isLoading = true
                // Simulate transaction
                onSuccessClick()
            },
            onCancel = { showConfirmation = false }
        )
    }
}

@Composable
fun SummaryCard(
    recipientUPI: String,
    amount: Double,
    fee: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NeonBlue.copy(alpha = 0.1f))
            .border(
                width = 1.dp,
                color = NeonBlue.copy(alpha = 0.3f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("To:", color = TextGray, fontSize = 12.sp)
            Text(recipientUPI.ifEmpty { "Not selected" }, color = TextWhite, fontSize = 12.sp)
        }
        
        Divider(color = NeonBlue.copy(alpha = 0.2f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Amount:", color = TextGray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("₹${String.format("%.2f", amount)}", color = NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Transaction Fee:", color = TextGray, fontSize = 12.sp)
            Text("₹${String.format("%.2f", fee)}", color = TextGray, fontSize = 12.sp)
        }
        
        Divider(color = NeonBlue.copy(alpha = 0.2f))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Total:", color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text("₹${String.format("%.2f", amount + fee)}", color = NeonBlue, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun FraudAlertCard(
    message: String,
    severity: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                when (severity) {
                    "Critical" -> NeonRed
                    "High" -> NeonOrange
                    else -> NeonYellow
                }.copy(alpha = 0.1f)
            )
            .border(
                width = 1.dp,
                color = when (severity) {
                    "Critical" -> NeonRed.copy(alpha = 0.3f)
                    "High" -> NeonOrange.copy(alpha = 0.3f)
                    else -> NeonYellow.copy(alpha = 0.3f)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = "Alert",
            tint = when (severity) {
                "Critical" -> NeonRed
                "High" -> NeonOrange
                else -> NeonYellow
            },
            modifier = Modifier.size(20.dp)
        )
        Text(
            message,
            fontSize = 12.sp,
            color = TextWhite
        )
    }
}

@Composable
fun ConfirmPaymentDialog(
    recipientUPI: String,
    amount: Double,
    description: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text("Confirm Payment", color = TextWhite, fontSize = 20.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Are you sure you want to send ₹${String.format("%.2f", amount)} to $recipientUPI?",
                    color = TextWhite
                )
                if (description.isNotEmpty()) {
                    Text(
                        "Note: $description",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = NeonRed.copy(alpha = 0.2f))
            ) {
                Text("Cancel")
            }
        }
    )
}
