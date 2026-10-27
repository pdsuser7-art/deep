package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CodeConsoleBg
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MetricMonoMedium
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SecondaryFixedDim
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.viewmodel.CompVantageUiState

@Composable
fun ExcelUploadFormulaSection(
    uiState: CompVantageUiState,
    onToggleAutoForward: (Boolean) -> Unit,
    onToggleHsnExclusion: (Boolean) -> Unit,
    onOpenUploadDialog: () -> Unit,
    onOpenFormulaGuide: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
    ) {
        // Section Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainerLow.copy(alpha = 0.6f), RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                .border(
                    width = 1.dp,
                    color = OutlineVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.TableChart,
                    contentDescription = null,
                    tint = RoyalBlue,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    Text(
                        text = "Excel Sales Batch Upload & Incentive Formula Configuration",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Engine rules mapped to Indian HSN codes, quarterly thresholds, and rollover quotas",
                        style = MaterialTheme.typography.labelSmall,
                        color = Outline
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen)
                        )
                        Text(
                            text = "HSN Engine: v4.2 Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clickable { onOpenFormulaGuide() }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                        .testTag("formula_syntax_guide_button"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Formula Syntax Guide",
                        tint = RoyalBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Formula Syntax Guide",
                        style = MaterialTheme.typography.labelSmall,
                        color = RoyalBlue
                    )
                }
            }
        }

        // Main Config Grid: Left (Upload Ledger) + Right (Toggles & Formula)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Uploaded Ledger Box (Left Side)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "ACTIVE UPLOADED LEDGER (.XLSX)",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Outline
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainerLow.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .border(1.dp, RoyalBlue.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(14.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                        .size(38.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(EmeraldGreen.copy(alpha = 0.1f))
                                        .border(1.dp, EmeraldGreen.copy(alpha = 0.25f), RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = uiState.activeLedger.fileName,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${uiState.activeLedger.sizeMb} MB • ${uiState.activeLedger.validatedRows} rows validated • ${uiState.activeLedger.sha256Check}",
                                        style = MetricMonoSmall.copy(fontSize = 11.sp),
                                        color = Outline
                                    )
                                }
                            }

                            IconButton(
                                onClick = onOpenUploadDialog,
                                modifier = Modifier.testTag("refresh_ledger_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Replace File",
                                    tint = Outline,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Validation Status Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
                                .border(1.dp, OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = EmeraldGreen,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "100% Schema Mapped",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = EmeraldGreen
                                        )
                                    }
                                    Text(
                                        text = "${uiState.activeLedger.validatedRows} / ${uiState.activeLedger.totalRows} Validated",
                                        style = MetricMonoSmall.copy(fontSize = 11.sp),
                                        color = Outline
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    uiState.activeLedger.schemaColumns.forEach { col ->
                                        Box(
                                            modifier = Modifier
                                                .background(SurfaceContainer, RoundedCornerShape(4.dp))
                                                .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "$col ✓",
                                                style = MetricMonoSmall.copy(fontSize = 10.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Drag/Upload Prompt
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Drop updated invoice sheet here to merge",
                                style = MaterialTheme.typography.labelSmall,
                                color = Outline
                            )
                            Row(
                                modifier = Modifier
                                    .clickable { onOpenUploadDialog() }
                                    .padding(vertical = 2.dp)
                                    .testTag("browse_files_button"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Browse Files",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = RoyalBlue
                                )
                                Icon(
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = "Upload",
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Toggles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Toggle 1: Auto-Forward Surplus
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(SurfaceContainerLow.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Switch(
                            checked = uiState.formulaConfig.autoForwardSurplus,
                            onCheckedChange = onToggleAutoForward,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = RoyalBlue,
                                uncheckedTrackColor = OutlineVariant
                            ),
                            modifier = Modifier.testTag("toggle_auto_forward")
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Auto-Forward Surplus to Q4",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .background(SecondaryFixed, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "Active - Surplus Logic",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = EmeraldGreen
                                    )
                                }
                            }
                            Text(
                                text = "Rolls over >100% achievement directly into Next Quarter's opening sales quota tally.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Outline
                            )
                        }
                    }
                }

                // Toggle 2: HSN Exclusion
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(SurfaceContainerLow.copy(alpha = 0.7f), RoundedCornerShape(6.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Switch(
                            checked = uiState.formulaConfig.hsnExclusionActive,
                            onCheckedChange = onToggleHsnExclusion,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = RoyalBlue,
                                uncheckedTrackColor = OutlineVariant
                            ),
                            modifier = Modifier.testTag("toggle_hsn_exclusion")
                        )
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "HSN Code Exclusion",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Box(
                                    modifier = Modifier
                                        .background(SurfaceContainer, RoundedCornerShape(3.dp))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "HSN 8471/8517",
                                        style = MetricMonoSmall.copy(fontSize = 9.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "Incentives applied to eligible commercial products; freight and warranty fees filtered out.",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Outline
                            )
                        }
                    }
                }
            }

            // Formula Expression Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
                    .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                    .padding(12.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Functions,
                                contentDescription = null,
                                tint = RoyalBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "ACTIVE COMMISSION FORMULA EXPRESSION",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Outline
                            )
                        }
                        Text(
                            text = "Calculated per rep ledger",
                            style = MetricMonoSmall.copy(fontSize = 11.sp),
                            color = Outline
                        )
                    }

                    // Console Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CodeConsoleBg, RoundedCornerShape(6.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "IF(Actual >= Target, Base_Incentive + (Actual - Target) * Tier_Bonus%, Base_Incentive * Penalty_Factor)",
                            style = MetricMonoSmall.copy(fontSize = 12.sp),
                            color = PrimaryFixedDim
                        )
                    }
                }
            }

            // Commission Tier Summary Matrix (3 Tiers)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tier 1
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(SurfaceContainerLow, RoundedCornerShape(6.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TIER 1 (80% - 99%)",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = Outline
                        )
                        Text(
                            text = "2.5% Rate",
                            style = MetricMonoMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "No Rollover",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Outline
                        )
                    }
                }

                // Tier 2
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(SurfaceContainerHigh.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .border(1.dp, RoyalBlue.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TIER 2 (100% - 119%)",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = RoyalBlue
                        )
                        Text(
                            text = "4.0% Rate",
                            style = MetricMonoMedium,
                            color = RoyalBlue
                        )
                        Text(
                            text = "+ Full Rollover",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = EmeraldGreen
                        )
                    }
                }

                // Tier 3
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(SecondaryFixed.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .border(1.dp, EmeraldGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TIER 3 (120%+)",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = EmeraldGreen
                        )
                        Text(
                            text = "5.5% Super",
                            style = MetricMonoMedium,
                            color = EmeraldGreen
                        )
                        Text(
                            text = "+ Full Rollover & Bonus",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = EmeraldGreen
                        )
                    }
                }
            }
        }
    }
}
