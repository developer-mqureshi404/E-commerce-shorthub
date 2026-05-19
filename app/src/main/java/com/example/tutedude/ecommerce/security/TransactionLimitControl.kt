package com.example.tutedude.ecommerce.security

import javax.inject.Inject
import javax.inject.Singleton

/**
 * TransactionLimitControl - Manages transaction limits and controls
 * File: TransactionLimitControl.kt
 * Purpose: Control transaction limits until approvals are completed
 */
@Singleton
class TransactionLimitControl @Inject constructor() {
    
    // Transaction limits in INR
    private val defaultDailyLimit = 100000.0
    private val defaultMonthlyLimit = 1000000.0
    private val defaultSingleTransactionLimit = 50000.0
    
    // Limits with approvals
    private val approvedDailyLimit = 500000.0
    private val approvedMonthlyLimit = 5000000.0
    private val approvedSingleTransactionLimit = 250000.0
    
    /**
     * Check if transaction is allowed based on limits
     */
    fun canProcessTransaction(
        amount: Double,
        dailyTotal: Double,
        monthlyTotal: Double,
        hasApprovals: Boolean
    ): TransactionLimitResult {
        val dailyLimit = if (hasApprovals) approvedDailyLimit else defaultDailyLimit
        val monthlyLimit = if (hasApprovals) approvedMonthlyLimit else defaultMonthlyLimit
        val singleLimit = if (hasApprovals) approvedSingleTransactionLimit else defaultSingleTransactionLimit
        
        return when {
            amount > singleLimit -> {
                TransactionLimitResult.Blocked(
                    "Single transaction limit exceeded. Max allowed: ₹${String.format("%.0f", singleLimit)}"
                )
            }
            dailyTotal + amount > dailyLimit -> {
                val remaining = dailyLimit - dailyTotal
                TransactionLimitResult.Blocked(
                    "Daily limit exceeded. Remaining for today: ₹${String.format("%.0f", remaining)}"
                )
            }
            monthlyTotal + amount > monthlyLimit -> {
                val remaining = monthlyLimit - monthlyTotal
                TransactionLimitResult.Blocked(
                    "Monthly limit exceeded. Remaining for month: ₹${String.format("%.0f", remaining)}"
                )
            }
            !hasApprovals && amount > 25000 -> {
                TransactionLimitResult.RequiresApproval(
                    "High-value transaction requires family member approval"
                )
            }
            else -> TransactionLimitResult.Allowed
        }
    }
    
    /**
     * Get remaining daily limit
     */
    fun getRemainingDailyLimit(
        dailyTotal: Double,
        hasApprovals: Boolean
    ): Double {
        val limit = if (hasApprovals) approvedDailyLimit else defaultDailyLimit
        return maxOf(0.0, limit - dailyTotal)
    }
    
    /**
     * Get remaining monthly limit
     */
    fun getRemainingMonthlyLimit(
        monthlyTotal: Double,
        hasApprovals: Boolean
    ): Double {
        val limit = if (hasApprovals) approvedMonthlyLimit else defaultMonthlyLimit
        return maxOf(0.0, limit - monthlyTotal)
    }
    
    /**
     * Get single transaction limit
     */
    fun getSingleTransactionLimit(hasApprovals: Boolean): Double {
        return if (hasApprovals) approvedSingleTransactionLimit else defaultSingleTransactionLimit
    }
    
    /**
     * Get message about approval requirement
     */
    fun getApprovalRequirementMessage(amount: Double): String? {
        return if (amount > 25000) {
            "Transaction amount ₹${String.format("%.0f", amount)} requires family member approval"
        } else {
            null
        }
    }
}

sealed class TransactionLimitResult {
    data object Allowed : TransactionLimitResult()
    data class RequiresApproval(val reason: String) : TransactionLimitResult()
    data class Blocked(val reason: String) : TransactionLimitResult()
}
