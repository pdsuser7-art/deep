package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SalesRepresentative
import com.example.model.formatIndianCurrency
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MetricMonoMedium
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurplusBg
import com.example.ui.theme.SurplusBorder
import com.example.ui.theme.SurplusText
import com.example.ui.theme.TableHeaderBg
import com.example.ui.theme.TableHeaderTextColor
import com.example.ui.theme.WarningBg
import com.example.ui.theme.WarningBorder
import com.example.ui.theme.WarningText
import com.example.viewmodel.CompVantageUiState

@Composable
fun IncentiveDataGrid(
    uiState: CompVantageUiState,
    onSearchChange: (String) -> Unit,
    onRegionSelect: (String) -> Unit,
    onRoleSelect: (String) -> Unit,
    onBulkRecalculate: () -> Unit,
    onDownloadExcelReport: () -> Unit,
    onAuditFormulaClick: (SalesRepresentative) -> Unit,
    onEditRepClick: (SalesRepresentative) -> Unit,
    onRecalculateSingleRep: (String) -> Unit,
    onPageSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var regionDropdownExpanded by remember { mutableStateOf(false) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var batchOutputDropdownExpanded by remember { mutableStateOf(false) }
    var selectedBatchOutput by remember { mutableStateOf(".XLSX Multi-Tab") }

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing)
        ),
        label = "rotation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
    ) {
        // Table Filter & Utility Toolbar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLow.copy(alpha = 0.3f), RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(RoyalBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = null,
                            tint = RoyalBlue,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Sales Executive Incentive & Target Achievement Grid",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Real-time calculations for FY24-25 Q3 with Indian Tax Withholding (TDS 194H)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Outline
                        )
                    }
                }
            }

            // Filters & Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Search Input inside Toolbar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = {
                        Text(
                            text = "Filter Rep name or EMP-ID...",
                            style = MaterialTheme.typography.labelSmall,
                            color = Outline
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Outline,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RoyalBlue,
                        unfocusedBorderColor = OutlineVariant,
                        focusedContainerColor = SurfaceContainerLowest,
                        unfocusedContainerColor = SurfaceContainerLowest
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .width(220.dp)
                        .height(36.dp)
                        .testTag("grid_search_filter")
                )

                // Region Hub Filter Dropdown
                Box {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                            .clickable { regionDropdownExpanded = true }
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.selectedRegion,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = regionDropdownExpanded,
                        onDismissRequest = { regionDropdownExpanded = false }
                    ) {
                        listOf(
                            "All Regions / Hubs",
                            "Mumbai",
                            "Delhi NCR",
                            "Bengaluru",
                            "Ahmedabad",
                            "Pune",
                            "Chandigarh",
                            "Kolkata",
                            "Chennai"
                        ).forEach { reg ->
                            DropdownMenuItem(
                                text = { Text(reg, style = MaterialTheme.typography.bodyMedium) },
                                onClick = {
                                    onRegionSelect(reg)
                                    regionDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Role Filter Dropdown
                Box {
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                            .clickable { roleDropdownExpanded = true }
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.selectedRole,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = roleDropdownExpanded,
                        onDismissRequest = { roleDropdownExpanded = false }
                    ) {
                        listOf(
                            "All Roles",
                            "Sr. Enterprise Sales Mgr",
                            "Key Account Exec",
                            "Area Sales Officer",
                            "Territory Lead",
                            "Sales Officer",
                            "Business Dev Exec"
                        ).forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r, style = MaterialTheme.typography.bodyMedium) },
                                onClick = {
                                    onRoleSelect(r)
                                    roleDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Bulk Recalculate Button
                Button(
                    onClick = onBulkRecalculate,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainerLowest,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                        .testTag("bulk_recalculate_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Autorenew,
                        contentDescription = "Bulk Recalculate",
                        tint = RoyalBlue,
                        modifier = Modifier
                            .size(16.dp)
                            .then(if (uiState.isRecalculating) Modifier.rotate(spinAngle) else Modifier)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Bulk Recalculate",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                // Download Excel Report Button
                Button(
                    onClick = onDownloadExcelReport,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("download_excel_report_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FileDownload,
                        contentDescription = "Download Excel Report",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Download Excel Report",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        // High-Density Data Table
        val tableScrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(tableScrollState)
        ) {
            Column(modifier = Modifier.width(1150.dp)) {
                // Table Header Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(TableHeaderBg)
                        .border(1.dp, OutlineVariant.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EMPLOYEE INFO & REGION",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        modifier = Modifier.width(260.dp)
                    )
                    Text(
                        text = "TARGET (₹)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(110.dp)
                    )
                    Text(
                        text = "ACTUAL SALES (₹)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(125.dp)
                    )
                    Text(
                        text = "TARGET ACHIEVED (%)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(155.dp)
                    )
                    Text(
                        text = "SURPLUS ROLLED TO Q4 (₹)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(155.dp)
                    )
                    Text(
                        text = "INCENTIVE RATE",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(115.dp)
                    )
                    Text(
                        text = "NET INCENTIVE (₹)",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(115.dp)
                    )
                    Text(
                        text = "STATUS & ACTIONS",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = TableHeaderTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(115.dp)
                    )
                }

                // Table Rows
                if (uiState.paginatedReps.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No sales representatives match the selected criteria.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Outline
                        )
                    }
                } else {
                    uiState.paginatedReps.forEachIndexed { index, rep ->
                        val isEven = index % 2 == 0
                        val rowBg = if (isEven) SurfaceContainerLowest else Color(0xFFFBFDFE)

                        val achievement = rep.achievementPercent(uiState.formulaConfig.hsnExclusionActive)
                        val surplus = rep.surplusAmount(uiState.formulaConfig.autoForwardSurplus, uiState.formulaConfig.hsnExclusionActive)
                        val tierLabel = rep.tierLabel(uiState.formulaConfig.hsnExclusionActive)
                        val incentive = rep.calculateIncentive(uiState.formulaConfig.hsnExclusionActive)
                        val tds = rep.calculateTds(uiState.formulaConfig.hsnExclusionActive)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(rowBg)
                                .border(1.dp, OutlineVariant.copy(alpha = 0.3f))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("rep_row_${rep.id.lowercase()}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Column 1: Employee Info & Region
                            Row(
                                modifier = Modifier.width(260.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceContainerHigh),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = rep.initials,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = RoyalBlue
                                    )
                                }
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = rep.name,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Box(
                                            modifier = Modifier
                                                .background(SurfaceContainer, RoundedCornerShape(2.dp))
                                                .padding(horizontal = 4.dp, vertical = 1.dp)
                                        ) {
                                            Text(
                                                text = rep.id,
                                                style = MetricMonoSmall.copy(fontSize = 10.sp),
                                                color = Outline
                                            )
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = rep.role,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = Outline
                                        )
                                        Text(
                                            text = "•",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Outline
                                        )
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Outline,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = rep.region,
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // Column 2: Target (₹)
                            Text(
                                text = formatIndianCurrency(rep.targetAmount),
                                style = MetricMonoMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.End,
                                modifier = Modifier.width(110.dp)
                            )

                            // Column 3: Actual Sales (₹)
                            Text(
                                text = formatIndianCurrency(rep.actualSales),
                                style = MetricMonoMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.End,
                                modifier = Modifier.width(125.dp)
                            )

                            // Column 4: Target Achieved (%) + Progress
                            Column(
                                modifier = Modifier.width(155.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Row(
                                    modifier = Modifier.width(130.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = String.format("%.1f%%", achievement),
                                        style = MetricMonoSmall.copy(fontSize = 11.sp),
                                        color = if (achievement >= 100.0) EmeraldGreen else ErrorRed
                                    )
                                    Text(
                                        text = if (achievement >= 100.0) {
                                            "Surplus +${String.format("%.1f%%", achievement - 100.0)}"
                                        } else {
                                            "Deficit -${String.format("%.1f%%", 100.0 - achievement)}"
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                        color = Outline
                                    )
                                }
                                LinearProgressIndicator(
                                    progress = { (achievement / 150.0).coerceIn(0.0, 1.0).toFloat() },
                                    modifier = Modifier
                                        .width(130.dp)
                                        .height(5.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = if (achievement >= 100.0) EmeraldGreen else ErrorRed,
                                    trackColor = SurfaceContainer
                                )
                            }

                            // Column 5: Surplus Rolled to Q4 (₹)
                            Box(
                                modifier = Modifier.width(155.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if (surplus > 0) {
                                    Box(
                                        modifier = Modifier
                                            .background(SurplusBg, RoundedCornerShape(4.dp))
                                            .border(1.dp, SurplusBorder, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.FastForward,
                                                contentDescription = null,
                                                tint = SurplusText,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text(
                                                text = "+${formatIndianCurrency(surplus)}",
                                                style = MetricMonoSmall.copy(fontSize = 11.sp),
                                                color = SurplusText
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .background(SurfaceContainer, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "₹0 (Nil)",
                                            style = MetricMonoSmall.copy(fontSize = 11.sp),
                                            color = Outline
                                        )
                                    }
                                }
                            }

                            // Column 6: Incentive Rate
                            Box(
                                modifier = Modifier.width(115.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val (bgRate, borderRate, textRate) = when {
                                    achievement >= 120.0 -> Triple(RoyalBlue.copy(alpha = 0.1f), RoyalBlue.copy(alpha = 0.25f), RoyalBlue)
                                    achievement >= 100.0 -> Triple(SurfaceContainer, OutlineVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                                    else -> Triple(SurfaceContainerLow, OutlineVariant, Outline)
                                }

                                Box(
                                    modifier = Modifier
                                        .background(bgRate, RoundedCornerShape(4.dp))
                                        .border(1.dp, borderRate, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = tierLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = textRate
                                    )
                                }
                            }

                            // Column 7: Net Incentive (₹)
                            Column(
                                modifier = Modifier.width(115.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = formatIndianCurrency(incentive),
                                    style = MetricMonoMedium,
                                    color = RoyalBlue
                                )
                                Text(
                                    text = "TDS -${formatIndianCurrency(tds)}",
                                    style = MetricMonoSmall.copy(fontSize = 10.sp),
                                    color = Outline
                                )
                            }

                            // Column 8: Status & Actions
                            Row(
                                modifier = Modifier.width(115.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                val isApproved = rep.status == "Approved" || uiState.isPayoutApproved
                                Box(
                                    modifier = Modifier
                                        .background(
                                            if (isApproved) SurplusBg else WarningBg,
                                            RoundedCornerShape(3.dp)
                                        )
                                        .border(
                                            1.dp,
                                            if (isApproved) SurplusBorder else WarningBorder,
                                            RoundedCornerShape(3.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isApproved) "Approved" else "Review Pending",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = if (isApproved) SurplusText else WarningText
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Audit Receipt Dialog Trigger
                                IconButton(
                                    onClick = { onAuditFormulaClick(rep) },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("audit_rep_${rep.id.lowercase()}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ReceiptLong,
                                        contentDescription = "Audit Formula Breakdown",
                                        tint = Outline,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Quick recalculate/edit button
                                IconButton(
                                    onClick = { onEditRepClick(rep) },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("edit_rep_${rep.id.lowercase()}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Adjust Target/Actual",
                                        tint = RoyalBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section 5: Footer & Pagination Summary Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLow.copy(alpha = 0.7f), RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(bottomStart = 6.dp, bottomEnd = 6.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Metric Summary Chips
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Total Reps:",
                        style = MaterialTheme.typography.labelSmall,
                        color = Outline
                    )
                    Text(
                        text = "${uiState.salesReps.size} Executives",
                        style = MetricMonoSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = Outline
                    )
                    Text(
                        text = "${uiState.qualifiedCount} Qualified (${((uiState.qualifiedCount.toDouble() / uiState.salesReps.size) * 100).toInt()}%)",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldGreen
                    )
                }

                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .width(1.dp)
                        .background(OutlineVariant)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Net Payout (Pre-TDS):",
                        style = MaterialTheme.typography.labelSmall,
                        color = Outline
                    )
                    Text(
                        text = formatIndianCurrency(uiState.totalIncentiveCalculated),
                        style = MetricMonoSmall.copy(fontSize = 11.sp),
                        color = RoyalBlue
                    )
                }

                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .width(1.dp)
                        .background(OutlineVariant)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Surplus Forwarded to Q4:",
                        style = MaterialTheme.typography.labelSmall,
                        color = Outline
                    )
                    Text(
                        text = "+${formatIndianCurrency(uiState.totalSurplusRolled)}",
                        style = MetricMonoSmall.copy(fontSize = 11.sp),
                        color = EmeraldGreen
                    )
                }
            }

            // Output Format Selector & Pagination Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Batch Output format dropdown
                Box {
                    Row(
                        modifier = Modifier
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                            .clickable { batchOutputDropdownExpanded = true }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Batch Output:",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Outline
                        )
                        Text(
                            text = selectedBatchOutput,
                            style = MetricMonoSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = batchOutputDropdownExpanded,
                        onDismissRequest = { batchOutputDropdownExpanded = false }
                    ) {
                        listOf(".XLSX Multi-Tab", "RBI NEFT Batch .CSV", "Tax Withholding (Form 16A)").forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt, style = MaterialTheme.typography.bodySmall) },
                                onClick = {
                                    selectedBatchOutput = opt
                                    batchOutputDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Pagination Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = { onPageSelect(uiState.currentPage - 1) },
                        enabled = uiState.currentPage > 1,
                        modifier = Modifier
                            .size(28.dp)
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                            .testTag("pagination_prev")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Page",
                            tint = if (uiState.currentPage > 1) MaterialTheme.colorScheme.onSurface else Outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    (1..uiState.totalPages).forEach { pageNum ->
                        val isCurrent = pageNum == uiState.currentPage
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isCurrent) RoyalBlue else SurfaceContainerLowest)
                                .border(1.dp, if (isCurrent) RoyalBlue else OutlineVariant, RoundedCornerShape(4.dp))
                                .clickable { onPageSelect(pageNum) }
                                .testTag("pagination_page_$pageNum"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$pageNum",
                                style = MetricMonoSmall.copy(fontSize = 11.sp),
                                color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    IconButton(
                        onClick = { onPageSelect(uiState.currentPage + 1) },
                        enabled = uiState.currentPage < uiState.totalPages,
                        modifier = Modifier
                            .size(28.dp)
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                            .testTag("pagination_next")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Page",
                            tint = if (uiState.currentPage < uiState.totalPages) MaterialTheme.colorScheme.onSurface else Outline,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
