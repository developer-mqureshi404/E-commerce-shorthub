package com.example.tutedude.ecommerce.presentation.payment

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
import com.example.tutedude.ecommerce.ui.theme.*

/**
 * ReceiveMoneyScreen - Display UPI ID and generate payment requests
 * File: ReceiveMoneyScreen.kt
 * Purpose: Allow users to receive money with easy UPI ID sharing
 */
@Composable
fun ReceiveMoneyScreen(
    userId: String,
    upiId: String = "user@hdfc",
    onBackClick: () -> Unit,
    onGenerateQRClick: () -> Unit
) {
    var shareMessage by remember { mutableStateOf("") }
    var copiedToClipboard by remember { mutableStateOf(false) }
    
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
                    "Receive Money",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            // Info Card
            InfoCard(
                icon = Icons.Default.Info,
                title = "Share Your UPI ID",
                description = "Others can send you money using your UPI ID",
                color = NeonBlue
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // UPI ID Display Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                NeonBlue.copy(alpha = 0.15f),
                                NeonPurple.copy(alpha = 0.15f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = NeonBlue,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "UPI",
                    tint = NeonBlue,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(bottom = 12.dp)
                )
                
                Text(
                    "Your UPI ID",
                    fontSize = 12.sp,
                    color = TextGray
                )
                
                Text(
                    upiId,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = NeonBlue,
                    modifier = Modifier.padding(vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
                
                // Copy Button
                Button(
                    onClick = {
                        // Copy to clipboard
                        copiedToClipboard = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonBlue.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, NeonBlue)
                ) {
                    Icon(
                        imageVector = if (copiedToClipboard) Icons.Default.Done else Icons.Default.ContentCopy,
                        contentDescription = "Copy",
                        tint = NeonBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (copiedToClipboard) "Copied!" else "Copy UPI ID",
                        fontSize = 12.sp,
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Share Options
            Text(
                "Share Via",
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
                ShareOptionButton(
                    icon = Icons.Default.Phone,
                    label = "SMS",
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                ShareOptionButton(
                    icon = Icons.Default.Email,
                    label = "Email",
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
                ShareOptionButton(
                    icon = Icons.Default.Share,
                    label = "More",
                    modifier = Modifier.weight(1f),
                    onClick = { }
                )
            }
            
            // QR Code Section
            Text(
                "QR Code for Payments",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .border(
                        width = 1.dp,
                        color = NeonBlue.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "QR",
                        tint = Color.Black,
                        modifier = Modifier.size(96.dp)
                    )
                    Text(
                        "QR Code",
                        color = Color.Black,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onGenerateQRClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = "Download",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Generate & Download QR",
                    fontSize = 14.sp,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Request Amount Card
            Text(
                "Request Specific Amount",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            TextField(
                value = shareMessage,
                onValueChange = { shareMessage = it },
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
                    Text("Enter amount (optional)", color = TextGray)
                },
                leadingIcon = {
                    Text("₹", color = NeonBlue, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedTextColor = TextWhite,
                    focusedTextColor = TextWhite
                ),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Request",
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Send Payment Request",
                    fontSize = 14.sp,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ShareOptionButton(
    icon: androidx.compose.material.icons.Icons,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(80.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonBlue.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.3f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = NeonBlue,
                modifier = Modifier.size(24.dp)
            )
            Text(
                label,
                fontSize = 11.sp,
                color = TextWhite,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
