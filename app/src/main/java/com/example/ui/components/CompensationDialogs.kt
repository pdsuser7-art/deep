package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AuditLogEntry
import com.example.model.SalesRepresentative
import com.example.model.formatIndianCurrency
import com.example.ui.theme.CodeConsoleBg
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MetricMonoLarge
import com.example.ui.theme.MetricMonoMedium
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurplusBg
import com.example.ui.theme.SurplusBorder
import com.example.ui.theme.SurplusText
import com.example.ui.theme.WarningBg
import com.example.ui.theme.WarningBorder
import com.example.ui.theme.WarningText

/**
 * Formula Audit Breakdown Modal
 */
@Composable
fun AuditBreakdownDialog(
    rep: SalesRepresentative,
    autoForwardSurplus: Boolean,
    hsnExclusionActive: Boolean,
    onRecalculate: () -> Unit,
    onDismiss: () -> Unit
) {
    val achievement = rep.achievementPercent(hsnExclusionActive)
    val surplus = rep.surplusAmount(autoForwardSurplus, hsnExclusionActive)
    val incentive = rep.calculateIncentive(hsnExclusionActive)
    val tds = rep.calculateTds(hsnExclusionActive)
    val net = rep.calculateNetPayable(hsnExclusionActive)
    val ratePct = rep.incentiveRate(hsnExclusionActive) * 100

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceContainerLowest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("audit_breakdown_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = RoyalBlue,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Formula Audit Breakdown",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${rep.name} (${rep.id}) • ${rep.role}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Outline
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Outline)
                    }
                }

                // Math Execution Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeConsoleBg, RoundedCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "// Active Formula Rule Applied:",
                            style = MetricMonoSmall.copy(fontSize = 11.sp),
                            color = OutlineVariant
                        )
                        Text(
                            text = "IF(Actual >= Target, Base (4.0%) + Surplus * Bonus ($ratePct%), Base * Penalty)",
                            style = MetricMonoSmall.copy(fontSize = 11.sp),
                            color = PrimaryFixedDim
                        )
                    }
                }

                // Metrics Breakdown List
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BreakdownLine(label = "Target Quota (₹):", value = formatIndianCurrency(rep.targetAmount))
                    BreakdownLine(label = "Actual Booked Sales (₹):", value = formatIndianCurrency(rep.actualSales))
                    if (hsnExclusionActive && rep.nonHsnDeduction > 0) {
                        BreakdownLine(
                            label = "Freight/Warranty Excluded (₹):",
                            value = "-${formatIndianCurrency(rep.nonHsnDeduction)}",
                            valueColor = ErrorRed
                        )
                    }
                    BreakdownLine(
                        label = "Achievement Percent:",
                        value = "${String.format("%.1f", achievement)}% (${rep.tierLabel(hsnExclusionActive)})",
                        valueColor = if (achievement >= 100.0) EmeraldGreen else ErrorRed
                    )
                    BreakdownLine(
                        label = "Surplus Rolled Forward to Q4:",
                        value = if (surplus > 0) "+${formatIndianCurrency(surplus)}" else "₹0 (Nil)",
                        valueColor = if (surplus > 0) EmeraldGreen else Outline
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(OutlineVariant.copy(alpha = 0.5f)))
                    BreakdownLine(
                        label = "Gross Incentive Computed:",
                        value = formatIndianCurrency(incentive),
                        valueColor = RoyalBlue
                    )
                    BreakdownLine(
                        label = "TDS Section 194H Withholding (10%):",
                        value = "-${formatIndianCurrency(tds)}",
                        valueColor = ErrorRed
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(OutlineVariant.copy(alpha = 0.5f)))
                    BreakdownLine(
                        label = "Net Payable via Bank NEFT:",
                        value = formatIndianCurrency(net),
                        valueColor = EmeraldGreen,
                        isBold = true
                    )
                }

                // Regulatory Note
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Outline, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Computed pursuant to Section 194H of Indian Income Tax Act. Retain voucher for Form 16A issuance.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                        color = Outline
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close", style = MaterialTheme.typography.labelSmall)
                    }

                    Button(
                        onClick = {
                            onRecalculate()
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Verify & Approve", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

/**
 * Execute Payout Approval Modal
 */
@Composable
fun PayoutApprovalDialog(
    qualifiedCount: Int,
    totalGrossSales: Long,
    totalIncentive: Long,
    totalTds: Long,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val netDisbursement = totalIncentive - totalTds

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = RoyalBlue,
                    modifier = Modifier.size(26.dp)
                )
                Text(
                    text = "Execute Q3 Payout Approval",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "You are authorizing final executive sign-off for FY 2024-25 Q3 Incentive Run #34. Once approved, the payout ledger will be locked and an RBI NEFT batch package will be issued.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    BreakdownLine("Total Gross Eligible Sales:", formatIndianCurrency(totalGrossSales))
                    BreakdownLine("Qualified Sales Executives:", "$qualifiedCount Executives")
                    BreakdownLine("Gross Calculated Commission:", formatIndianCurrency(totalIncentive), valueColor = RoyalBlue)
                    BreakdownLine("Income Tax TDS (194H) Withheld:", "-${formatIndianCurrency(totalTds)}", valueColor = ErrorRed)
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(OutlineVariant.copy(alpha = 0.5f)))
                    BreakdownLine("Net Disbursement to Bank:", formatIndianCurrency(netDisbursement), valueColor = EmeraldGreen, isBold = true)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.testTag("confirm_payout_approval")
            ) {
                Text("Sign & Authorize Bank Transfer", style = MaterialTheme.typography.labelSmall)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Cancel", style = MaterialTheme.typography.labelSmall)
            }
        }
    )
}

/**
 * Edit Representative Target & Actual Sales Dialog
 */
@Composable
fun EditRepDialog(
    rep: SalesRepresentative,
    onSave: (Long, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var targetText by remember { mutableStateOf(rep.targetAmount.toString()) }
    var actualText by remember { mutableStateOf(rep.actualSales.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Adjust Quota / Actuals: ${rep.name}",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "Update sales quota or verified ledger achievement for ${rep.id} (${rep.region}). Grid will automatically recalculate tier bonus and tax withholding.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Outline
                )

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it.filter { char -> char.isDigit() } },
                    label = { Text("Target Quota (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = actualText,
                    onValueChange = { actualText = it.filter { char -> char.isDigit() } },
                    label = { Text("Actual Sales Booked (₹)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newTarget = targetText.toLongOrNull() ?: rep.targetAmount
                    val newActual = actualText.toLongOrNull() ?: rep.actualSales
                    onSave(newTarget, newActual)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Save & Recalculate", style = MaterialTheme.typography.labelSmall)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(4.dp)) {
                Text("Cancel", style = MaterialTheme.typography.labelSmall)
            }
        }
    )
}

/**
 * Upload New Excel File Dialog
 */
@Composable
fun UploadExcelDialog(
    onUpload: (fileName: String, rowCount: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedName by remember { mutableStateOf("Q3_North_West_Sales_Ledger_Final_Merged.xlsx") }
    var rowCountInput by remember { mutableStateOf("3840") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.UploadFile, contentDescription = null, tint = RoyalBlue)
                Text("Upload Excel Sales Ledger", style = MaterialTheme.typography.headlineSmall)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Upload updated sales ledger (.xlsx) to compute multi-tier commissions with HSN code validation.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Outline
                )

                OutlinedTextField(
                    value = selectedName,
                    onValueChange = { selectedName = it },
                    label = { Text("Excel File Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = rowCountInput,
                    onValueChange = { rowCountInput = it.filter { c -> c.isDigit() } },
                    label = { Text("Total Validated Invoice Rows") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Mandatory Columns: Emp_ID, Invoice_Date, HSN_Code, Net_Sales_Value (₹)",
                        style = MetricMonoSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rows = rowCountInput.toIntOrNull() ?: 3420
                    onUpload(selectedName, rows)
                },
                colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text("Verify & Map Schema", style = MaterialTheme.typography.labelSmall)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(4.dp)) {
                Text("Cancel", style = MaterialTheme.typography.labelSmall)
            }
        }
    )
}

/**
 * Export to Bank NEFT Dialog
 */
@Composable
fun ExportNeftDialog(
    reps: List<SalesRepresentative>,
    hsnExclusionActive: Boolean,
    onDismiss: () -> Unit
) {
    val qualifiedReps = reps.filter { it.achievementPercent(hsnExclusionActive) >= 100.0 }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceContainerLowest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = RoyalBlue)
                        Text(
                            text = "RBI NEFT Bank Authorization Sheet",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Outline)
                    }
                }

                Text(
                    text = "Standard corporate banking format for automated salary & incentive disbursement through ICICI / HDFC Corporate Banking portal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Outline
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CodeConsoleBg, RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "RECORD_TYPE|BENEFICIARY_NAME|IFSC|ACCOUNT_NO|NET_AMOUNT|NARRATION",
                        style = MetricMonoSmall.copy(fontSize = 10.sp),
                        color = SecondaryFixed
                    )
                    qualifiedReps.take(5).forEach { rep ->
                        val net = rep.calculateNetPayable(hsnExclusionActive)
                        Text(
                            text = "NEFT|${rep.name.uppercase()}|HDFC0001245|501004928192|${net}|FY2425Q3_INCENTIVE",
                            style = MetricMonoSmall.copy(fontSize = 9.sp),
                            color = PrimaryFixedDim
                        )
                    }
                    if (qualifiedReps.size > 5) {
                        Text(
                            text = "... and ${qualifiedReps.size - 5} more qualified beneficiary rows",
                            style = MetricMonoSmall.copy(fontSize = 9.sp),
                            color = Outline
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Download NEFT Batch (.CSV)", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

/**
 * Formula Syntax Guide Dialog
 */
@Composable
fun FormulaGuideDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceContainerLowest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Formula Syntax & Slabs Guide",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Outline)
                    }
                }

                Text(
                    text = "CompVantage IN calculates sales performance management (SPM) payouts using an Indian regulatory-compliant tiered schema:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "1. Tier 1 (80% - 99% Achievement):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "2.5% Base rate applied to actual net sales. No surplus rollover allowed.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Outline
                    )

                    Text(
                        text = "2. Tier 2 (100% - 119% Achievement):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "4.0% Standard commission rate. Excess sales above 100% are auto-rolled over into Next Quarter opening ledger.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Outline
                    )

                    Text(
                        text = "3. Tier 3 (120%+ Overachievement):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "5.5% Super Tier rate applied on surplus delta. 100% surplus credited to Q4.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Outline
                    )

                    Text(
                        text = "4. Tax Withholding (TDS Section 194H):",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Indian Income Tax prescribes 10% TDS deduction for payouts exceeding ₹15,000 threshold in a financial year.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Outline
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Got It", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

/**
 * Audit Logs Dialog
 */
@Composable
fun AuditLogsDialog(
    logs: List<AuditLogEntry>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = SurfaceContainerLowest,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.HistoryEdu, contentDescription = null, tint = RoyalBlue)
                        Text(
                            text = "Compliance Audit Logs",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Outline)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    logs.forEach { log ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceContainerLow, RoundedCornerShape(4.dp))
                                .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = log.action,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = RoyalBlue
                                    )
                                    Text(
                                        text = log.timestamp,
                                        style = MetricMonoSmall.copy(fontSize = 10.sp),
                                        color = Outline
                                    )
                                }
                                Text(
                                    text = "Operator: ${log.operator} • Run: ${log.runId}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = log.details,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = log.batchHash,
                                    style = MetricMonoSmall.copy(fontSize = 9.sp),
                                    color = Outline
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close Audit Trail", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun BreakdownLine(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isBold) MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold) else MaterialTheme.typography.bodySmall,
            color = if (isBold) MaterialTheme.colorScheme.onSurface else Outline
        )
        Text(
            text = value,
            style = MetricMonoMedium.copy(fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium),
            color = valueColor
        )
    }
}
