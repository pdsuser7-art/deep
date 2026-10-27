package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.formatIndianCurrency
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MetricMonoMedium
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.viewmodel.CompVantageUiState

@Composable
fun ExcelUploadScreen(
    uiState: CompVantageUiState,
    onOpenUploadDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.UploadFile, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
            Column {
                Text("Excel Sales Upload & Ingestion Engine", style = MaterialTheme.typography.headlineMedium)
                Text("Upload quarterly sales transaction sheets with automatic schema mapping and Indian GST/HSN verification.", style = MaterialTheme.typography.bodySmall, color = Outline)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLow, RoundedCornerShape(8.dp))
                .border(2.dp, RoyalBlue.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Description, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(48.dp))
                Text(uiState.activeLedger.fileName, style = MaterialTheme.typography.labelLarge)
                Text("Size: ${uiState.activeLedger.sizeMb} MB • ${uiState.activeLedger.validatedRows} rows validated • SHA-256 Signature Verified", style = MetricMonoSmall, color = Outline)
                Spacer(modifier = Modifier.height(6.dp))
                Button(
                    onClick = onOpenUploadDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Upload & Merge New Sheet (.xlsx)")
                }
            }
        }

        Text("Required Indian SPM Columns:", style = MaterialTheme.typography.labelMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.activeLedger.schemaColumns.forEach { col ->
                Box(
                    modifier = Modifier
                        .background(SurfaceContainer, RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("$col (Validated)", style = MetricMonoSmall, color = EmeraldGreen)
                }
            }
        }
    }
}

@Composable
fun EmployeesAttendanceScreen(
    uiState: CompVantageUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Badge, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
            Column {
                Text("Sales Executives & Roster Directory", style = MaterialTheme.typography.headlineMedium)
                Text("${uiState.salesReps.size} active revenue professionals across 8 regional hubs in India", style = MaterialTheme.typography.bodySmall, color = Outline)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.salesReps.take(6).forEach { rep ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .padding(12.dp),
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
                                .clip(RoundedCornerShape(4.dp))
                                .background(RoyalBlue.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(rep.initials, style = MaterialTheme.typography.labelSmall, color = RoyalBlue)
                        }
                        Column {
                            Text(rep.name, style = MaterialTheme.typography.labelMedium)
                            Text("${rep.id} • ${rep.role} • ${rep.region}", style = MaterialTheme.typography.bodySmall, color = Outline)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("Target: ${formatIndianCurrency(rep.targetAmount)}", style = MetricMonoSmall)
                        Text(
                            text = "${String.format("%.1f", rep.achievementPercent(uiState.formulaConfig.hsnExclusionActive))}% Achieved",
                            style = MetricMonoSmall,
                            color = if (rep.achievementPercent(uiState.formulaConfig.hsnExclusionActive) >= 100.0) EmeraldGreen else Outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HsnCatalogScreen(
    uiState: CompVantageUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Inventory2, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
            Column {
                Text("Product & Indian HSN Tax Catalog", style = MaterialTheme.typography.headlineMedium)
                Text("Map product categories and GST rates for automated commission eligibility and deductions", style = MaterialTheme.typography.bodySmall, color = Outline)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            uiState.hsnCatalog.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("HSN ${item.hsnCode}", style = MetricMonoMedium, color = RoyalBlue)
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (item.isEligible) EmeraldGreen.copy(alpha = 0.15f) else Color(0xFFFFDAD6),
                                        RoundedCornerShape(3.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (item.isEligible) "Commission Eligible" else "Excluded",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = if (item.isEligible) EmeraldGreen else Color(0xFFBA1A1A)
                                )
                            }
                        }
                        Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                        Text("Category: ${item.category} • GST: ${item.gstRatePercent}%", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = Outline)
                    }

                    Text("Q3 Volume: ${item.quarterlyVolume}", style = MetricMonoMedium, color = RoyalBlue)
                }
            }
        }
    }
}

@Composable
fun PayoutReportsScreen(
    uiState: CompVantageUiState,
    onExportNeft: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                Icon(Icons.Default.Payments, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
                Column {
                    Text("Payout Reports & Tax Statements", style = MaterialTheme.typography.headlineMedium)
                    Text("Pre-TDS calculations, Section 194H withholding records, and RBI NEFT payment batches", style = MaterialTheme.typography.bodySmall, color = Outline)
                }
            }

            Button(
                onClick = onExportNeft,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Export Bank NEFT")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text("GROSS PRE-TDS INCENTIVE", style = MaterialTheme.typography.labelSmall, color = Outline)
                    Text(formatIndianCurrency(uiState.totalIncentiveCalculated), style = MetricMonoMedium, color = RoyalBlue)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text("TDS 194H WITHHELD", style = MaterialTheme.typography.labelSmall, color = Outline)
                    Text(formatIndianCurrency(uiState.totalTdsWithholding), style = MetricMonoMedium, color = Color(0xFFBA1A1A))
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text("NET NEFT DISBURSED", style = MaterialTheme.typography.labelSmall, color = Outline)
                    Text(formatIndianCurrency(uiState.totalNetPayout), style = MetricMonoMedium, color = EmeraldGreen)
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    uiState: CompVantageUiState,
    onToggleAutoForward: (Boolean) -> Unit,
    onToggleHsnExclusion: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = RoyalBlue, modifier = Modifier.size(28.dp))
            Column {
                Text("Enterprise Compensation Governance Settings", style = MaterialTheme.typography.headlineMedium)
                Text("Configure commission tiers, tax thresholds, and rollover quotas", style = MaterialTheme.typography.bodySmall, color = Outline)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Auto-Forward Surplus to Q4", style = MaterialTheme.typography.labelLarge)
                Text("Roll over >100% quota achievement into next quarter opening credit.", style = MaterialTheme.typography.bodySmall, color = Outline)
            }
            Switch(
                checked = uiState.formulaConfig.autoForwardSurplus,
                onCheckedChange = onToggleAutoForward,
                colors = SwitchDefaults.colors(checkedTrackColor = RoyalBlue)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("HSN Code Exclusion (8471/8517)", style = MaterialTheme.typography.labelLarge)
                Text("Deduct freight and warranty fees from commission base automatically.", style = MaterialTheme.typography.bodySmall, color = Outline)
            }
            Switch(
                checked = uiState.formulaConfig.hsnExclusionActive,
                onCheckedChange = onToggleHsnExclusion,
                colors = SwitchDefaults.colors(checkedTrackColor = RoyalBlue)
            )
        }
    }
}
