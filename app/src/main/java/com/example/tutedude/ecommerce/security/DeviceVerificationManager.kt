package com.example.tutedude.ecommerce.security

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DeviceVerificationManager - Manages device verification and fingerprinting
 * File: DeviceVerificationManager.kt
 * Purpose: Verify device identity and detect compromised devices
 */
@Singleton
class DeviceVerificationManager @Inject constructor(
    private val context: Context
) {
    
    /**
     * Generate device fingerprint
     */
    fun generateDeviceFingerprint(): String {
        val deviceInfo = StringBuilder()
        
        // Add device-specific information
        deviceInfo.append(Build.DEVICE)
        deviceInfo.append("|")
        deviceInfo.append(Build.MANUFACTURER)
        deviceInfo.append("|")
        deviceInfo.append(Build.MODEL)
        deviceInfo.append("|")
        deviceInfo.append(Build.PRODUCT)
        deviceInfo.append("|")
        deviceInfo.append(Build.VERSION.SDK_INT)
        deviceInfo.append("|")
        deviceInfo.append(getAndroidId())
        
        return hashString(deviceInfo.toString())
    }
    
    /**
     * Get Android ID
     */
    private fun getAndroidId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }
    
    /**
     * Hash string using SHA256
     */
    private fun hashString(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
    
    /**
     * Get device model info
     */
    fun getDeviceModel(): String {
        return "${Build.MANUFACTURER} ${Build.MODEL}"
    }
    
    /**
     * Get OS version
     */
    fun getOSVersion(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }
    
    /**
     * Check if device is rooted
     */
    fun isDeviceRooted(): Boolean {
        return checkForRootBinary() || checkForMagiskBinary() || checkForSuperuserApp()
    }
    
    private fun checkForRootBinary(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su"
        )
        
        return paths.any { java.io.File(it).exists() }
    }
    
    private fun checkForMagiskBinary(): Boolean {
        return java.io.File("/data/adb/magisk").exists()
    }
    
    private fun checkForSuperuserApp(): Boolean {
        return try {
            context.packageManager.getPackageInfo("com.noshufou.android.su", 0)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Verify device against stored fingerprint
     */
    fun verifyDeviceFingerprint(storedFingerprint: String): Boolean {
        val currentFingerprint = generateDeviceFingerprint()
        return currentFingerprint == storedFingerprint
    }
    
    /**
     * Calculate device trust score (0-100)
     */
    fun calculateTrustScore(): Float {
        var score = 100f
        
        if (isDeviceRooted()) {
            score -= 50f
        }
        
        if (Build.VERSION.SDK_INT < 28) {
            score -= 20f  // Older Android version
        }
        
        return maxOf(0f, score)
    }
    
    /**
     * Get complete device verification status
     */
    fun getDeviceVerificationStatus(): DeviceVerificationStatus {
        val isRooted = isDeviceRooted()
        val trustScore = calculateTrustScore()
        val fingerprint = generateDeviceFingerprint()
        
        return DeviceVerificationStatus(
            deviceModel = getDeviceModel(),
            osVersion = getOSVersion(),
            isRooted = isRooted,
            trustScore = trustScore,
            fingerprint = fingerprint,
            isCompromised = isRooted || trustScore < 50f
        )
    }
}

data class DeviceVerificationStatus(
    val deviceModel: String,
    val osVersion: String,
    val isRooted: Boolean,
    val trustScore: Float,
    val fingerprint: String,
    val isCompromised: Boolean
)
