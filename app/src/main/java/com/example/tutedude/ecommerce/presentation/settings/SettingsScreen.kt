package com.example.tutedude.ecommerce.presentation.settings

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
 * SettingsScreen - User settings and preferences
 * File: SettingsScreen.kt
 * Purpose: Handle dark mode, security settings, and app preferences
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    var darkModeEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var selectedTheme by remember { mutableStateOf("Dark") }
    
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
        ) {
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
                    "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }
            
            Divider(color = NeonBlue.copy(alpha = 0.1f))
            
            // Profile Section
            SettingsSectionHeader("Account")
            
            SettingsMenuItem(
                icon = Icons.Default.Person,
                title = "Profile & KYC",
                subtitle = "Update your profile information",
                onClick = onProfileClick
            )
            
            SettingsMenuItem(
                icon = Icons.Default.Lock,
                title = "Security",
                subtitle = "Manage security settings",
                onClick = onSecurityClick
            )
            
            Divider(
                color = NeonBlue.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
            
            // Display Section
            SettingsSectionHeader("Display & Experience")
            
            SettingsToggleItem(
                icon = Icons.Default.DarkMode,
                title = "Dark Mode",
                subtitle = "Always on",
                enabled = darkModeEnabled,
                onToggle = { darkModeEnabled = it }
            )
            
            SettingsDropdownItem(
                icon = Icons.Default.Palette,
                title = "Theme",
                selectedValue = selectedTheme,
                options = listOf("Dark", "Light", "Auto"),
                onSelectionChange = { selectedTheme = it }
            )
            
            Divider(
                color = NeonBlue.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
            
            // Security Section
            SettingsSectionHeader("Security & Privacy")
            
            SettingsToggleItem(
                icon = Icons.Default.Fingerprint,
                title = "Biometric Authentication",
                subtitle = "Use fingerprint or face recognition",
                enabled = biometricEnabled,
                onToggle = { biometricEnabled = it }
            )
            
            SettingsMenuItem(
                icon = Icons.Default.VpnKey,
                title = "Change PIN",
                subtitle = "Update your transaction PIN",
                onClick = { }
            )
            
            SettingsMenuItem(
                icon = Icons.Default.Security,
                title = "Device Verification",
                subtitle = "Verify this device",
                onClick = { }
            )
            
            Divider(
                color = NeonBlue.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
            
            // Notifications Section
            SettingsSectionHeader("Notifications")
            
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Receive transaction alerts",
                enabled = notificationsEnabled,
                onToggle = { notificationsEnabled = it }
            )
            
            SettingsMenuItem(
                icon = Icons.Default.Email,
                title = "Email Preferences",
                subtitle = "Manage email notifications",
                onClick = { }
            )
            
            Divider(
                color = NeonBlue.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
            
            // Support Section
            SettingsSectionHeader("Support & About")
            
            SettingsMenuItem(
                icon = Icons.Default.Help,
                title = "Help & Support",
                subtitle = "Get help with your account",
                onClick = onHelpClick
            )
            
            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "About GnardeWallet",
                subtitle = "Version 1.0.0",
                onClick = { }
            )
            
            SettingsMenuItem(
                icon = Icons.Default.VerifiedUser,
                title = "Terms & Privacy",
                subtitle = "Read our policies",
                onClick = { }
            )
            
            Divider(
                color = NeonBlue.copy(alpha = 0.1f),
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
            
            // Logout Button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonRed.copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, NeonRed)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = NeonRed,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Logout",
                    fontSize = 16.sp,
                    color = NeonRed,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = NeonBlue,
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    )
}

@Composable
fun SettingsMenuItem(
    icon: androidx.compose.material.icons.Icons,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = NeonBlue,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open",
            tint = NeonBlue.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsToggleItem(
    icon: androidx.compose.material.icons.Icons,
    title: String,
    subtitle: String,
    enabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (enabled) NeonBlue else TextGray,
                modifier = Modifier.size(24.dp)
            )
            Column {
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
        }
        Switch(
            checked = enabled,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonBlue,
                checkedTrackColor = NeonBlue.copy(alpha = 0.3f)
            )
        )
    }
}

@Composable
fun SettingsDropdownItem(
    icon: androidx.compose.material.icons.Icons,
    title: String,
    selectedValue: String,
    options: List<String>,
    onSelectionChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(GlassLight.copy(alpha = 0.3f))
                .border(
                    width = 1.dp,
                    color = NeonBlue.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                )
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = NeonBlue,
                    modifier = Modifier.size(24.dp)
                )
                Column {
                    Text(
                        title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextGray,
                        fontSize = 12.sp
                    )
                    Text(
                        selectedValue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Expand",
                tint = NeonBlue,
                modifier = Modifier.size(20.dp)
            )
        }
        
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GlassLight.copy(alpha = 0.5f))
                    .border(
                        width = 1.dp,
                        color = NeonBlue.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                options.forEach { option ->
                    Text(
                        option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelectionChange(option)
                                expanded = false
                            }
                            .padding(12.dp),
                        color = if (option == selectedValue) NeonBlue else TextWhite,
                        fontWeight = if (option == selectedValue) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
