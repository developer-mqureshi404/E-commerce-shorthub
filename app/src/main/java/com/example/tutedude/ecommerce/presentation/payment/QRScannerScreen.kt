package com.example.tutedude.ecommerce.presentation.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * QRScannerScreen - QR code scanner for UPI payments
 * File: QRScannerScreen.kt
 * Purpose: Scan QR codes for quick payments and transaction initiation
 */
@Composable
fun QRScannerScreen(
    userId: String,
    onBackClick: () -> Unit,
    onQRScanned: (String) -> Unit,
    onSendClick: () -> Unit
) {
    var scannedData by remember { mutableStateOf<String?>(null) }
    var isScanning by remember { mutableStateOf(false) }
    var cameraPermissionGranted by remember { mutableStateOf(false) }
    var torchEnabled by remember { mutableStateOf(false) }
    
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
                    "Scan QR Code",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { torchEnabled = !torchEnabled }) {
                    Icon(
                        imageVector = if (torchEnabled) Icons.Default.FlashlightOff else Icons.Default.FlashlightOn,
                        contentDescription = "Torch",
                        tint = if (torchEnabled) NeonYellow else NeonBlue
                    )
                }
            }
            
            if (!cameraPermissionGranted) {
                // Permission Request View
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            tint = NeonBlue,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp)
                        )
                        Text(
                            "Camera Permission Required",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            "We need camera access to scan QR codes for payments",
                            fontSize = 14.sp,
                            color = TextGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Button(
                            onClick = { cameraPermissionGranted = true },
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "Grant Permission",
                                fontSize = 16.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                // Camera Preview Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.Black.copy(alpha = 0.5f))
                        .border(
                            width = 2.dp,
                            color = NeonBlue,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isScanning) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = NeonBlue,
                                modifier = Modifier
                                    .size(64.dp)
                                    .padding(bottom = 16.dp)
                            )
                            Text(
                                "Point camera at QR code",
                                fontSize = 16.sp,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Scanning will start automatically",
                                fontSize = 12.sp,
                                color = TextGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // QR Scanner frame simulation
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .border(
                                        width = 3.dp,
                                        color = NeonGreen,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            )
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(300.dp)
                                    .align(Alignment.Center),
                                color = NeonBlue.copy(alpha = 0.3f),
                                strokeWidth = 4.dp
                            )
                        }
                    }
                }
                
                // Scanned Result
                if (scannedData != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonGreen.copy(alpha = 0.1f))
                                .border(
                                    width = 1.dp,
                                    color = NeonGreen.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = NeonGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                "QR Code scanned successfully!",
                                color = TextWhite,
                                fontSize = 12.sp
                            )
                        }
                        
                        Button(
                            onClick = {
                                onQRScanned(scannedData!!)
                                onSendClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Proceed to Payment", color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = { scannedData = null },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonBlue.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, NeonBlue)
                        ) {
                            Text("Scan Another", color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Scanning controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { isScanning = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !isScanning
                        ) {
                            Text("Start Scan", color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        
                        Button(
                            onClick = { onBackClick() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonRed.copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, NeonRed)
                        ) {
                            Text("Cancel", color = NeonRed, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// For manual UPI entry fallback
@Composable
fun ManualUPIEntryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var upiId by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Enter UPI ID", color = TextWhite, fontSize = 20.sp)
        },
        text = {
            TextField(
                value = upiId,
                onValueChange = { upiId = it },
                placeholder = {
                    Text("example@bank", color = TextGray)
                },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = TextWhite,
                    focusedTextColor = TextWhite
                )
            )
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(upiId) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
            ) {
                Text("Continue")
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
