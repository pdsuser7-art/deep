package com.example.model

import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToLong

data class SalesRepresentative(
    val id: String,
    val name: String,
    val initials: String,
    val role: String,
    val region: String,
    val targetAmount: Long,
    val actualSales: Long,
    val status: String = "Approved",
    val nonHsnDeduction: Long = 0L // Freight/warranty excluded when HSN filter is on
) {
    fun effectiveActual(hsnExclusionActive: Boolean): Long {
        return if (hsnExclusionActive) {
            max(0L, actualSales - nonHsnDeduction)
        } else {
            actualSales
        }
    }

    fun achievementPercent(hsnExclusionActive: Boolean): Double {
        if (targetAmount <= 0) return 0.0
        val effective = effectiveActual(hsnExclusionActive)
        return (effective.toDouble() / targetAmount.toDouble()) * 100.0
    }

    fun surplusAmount(autoForwardActive: Boolean, hsnExclusionActive: Boolean): Long {
        if (!autoForwardActive) return 0L
        val effective = effectiveActual(hsnExclusionActive)
        return if (effective > targetAmount) effective - targetAmount else 0L
    }

    fun tierLabel(hsnExclusionActive: Boolean): String {
        val pct = achievementPercent(hsnExclusionActive)
        return when {
            pct >= 120.0 -> "5.5% Super Tier"
            pct >= 100.0 -> "4.0% Tier 2"
            pct >= 80.0 -> "2.5% Base"
            else -> "Deficit"
        }
    }

    fun incentiveRate(hsnExclusionActive: Boolean): Double {
        val pct = achievementPercent(hsnExclusionActive)
        return when {
            pct >= 120.0 -> 0.055
            pct >= 100.0 -> 0.040
            pct >= 80.0 -> 0.025
            else -> 0.015
        }
    }

    /**
     * Commission calculation logic from active formula expression:
     * IF(Actual >= Target, Base_Incentive + (Actual - Target) * Tier_Bonus%, Base_Incentive * Penalty_Factor)
     */
    fun calculateIncentive(hsnExclusionActive: Boolean): Long {
        val effective = effectiveActual(hsnExclusionActive)
        val rate = incentiveRate(hsnExclusionActive)
        return if (effective >= targetAmount) {
            // Base on target + bonus on surplus
            val baseIncentive = (targetAmount * 0.040).roundToLong()
            val surplus = effective - targetAmount
            val surplusBonus = (surplus * rate).roundToLong()
            baseIncentive + surplusBonus
        } else {
            val baseIncentive = (effective * rate).roundToLong()
            baseIncentive
        }
    }

    /**
     * Section 194H TDS withholding: 10% on commission exceeding ₹15,000 threshold
     */
    fun calculateTds(hsnExclusionActive: Boolean): Long {
        val incentive = calculateIncentive(hsnExclusionActive)
        return if (incentive > 15000L) {
            (incentive * 0.10).roundToLong()
        } else {
            0L
        }
    }

    fun calculateNetPayable(hsnExclusionActive: Boolean): Long {
        return calculateIncentive(hsnExclusionActive) - calculateTds(hsnExclusionActive)
    }
}

data class ExcelLedgerFile(
    val fileName: String,
    val sizeMb: Double,
    val validatedRows: Int,
    val totalRows: Int,
    val sha256Check: String,
    val schemaColumns: List<String>,
    val uploadDate: String
)

data class FormulaConfig(
    val autoForwardSurplus: Boolean = true,
    val hsnExclusionActive: Boolean = true,
    val tier1Threshold: Double = 80.0,
    val tier1Rate: Double = 2.5,
    val tier2Threshold: Double = 100.0,
    val tier2Rate: Double = 4.0,
    val tier3Threshold: Double = 120.0,
    val tier3Rate: Double = 5.5,
    val tdsRatePercent: Double = 10.0,
    val tdsThreshold: Long = 15000L
)

data class AuditLogEntry(
    val runId: String,
    val timestamp: String,
    val operator: String,
    val action: String,
    val details: String,
    val batchHash: String
)

data class ProductHsn(
    val hsnCode: String,
    val description: String,
    val category: String,
    val gstRatePercent: Int,
    val isEligible: Boolean,
    val quarterlyVolume: String
)

enum class ScreenDestination(val title: String, val iconName: String) {
    INCENTIVE_ENGINE("Incentive Engine", "calculate"),
    DASHBOARD("Dashboard", "dashboard"),
    EXCEL_UPLOAD("Excel Sales Upload", "upload_file"),
    EMPLOYEES("Employees & Attendance", "badge"),
    HSN_CATALOG("Product & HSN Catalog", "inventory_2"),
    PAYOUT_REPORTS("Payout Reports", "payments"),
    SETTINGS("Settings", "settings")
}

/**
 * Format currency numbers in Indian numbering system (Lakhs, Crores)
 * e.g., ₹4,82,50,000
 */
fun formatIndianCurrency(amount: Long, prefix: String = "₹"): String {
    val isNegative = amount < 0
    val absAmount = if (isNegative) -amount else amount
    val str = absAmount.toString()
    val len = str.length

    val formatted = if (len <= 3) {
        str
    } else {
        val last3 = str.substring(len - 3)
        val rest = str.substring(0, len - 3)
        val sb = StringBuilder()
        var count = 0
        for (i in rest.length - 1 downTo 0) {
            sb.append(rest[i])
            count++
            if (count % 2 == 0 && i > 0) {
                sb.append(',')
            }
        }
        sb.reverse().toString() + "," + last3
    }

    return if (isNegative) "-$prefix$formatted" else "$prefix$formatted"
}

fun formatCrLakhs(amount: Long): String {
    return when {
        amount >= 10000000L -> {
            val cr = amount.toDouble() / 10000000.0
            String.format(Locale.US, "₹%.2f Cr", cr)
        }
        amount >= 100000L -> {
            val lakhs = amount.toDouble() / 100000.0
            String.format(Locale.US, "₹%.1f L", lakhs)
        }
        else -> formatIndianCurrency(amount)
    }
}
