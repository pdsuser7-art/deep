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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.formatCrLakhs
import com.example.model.formatIndianCurrency
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MetricMonoLarge
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurplusBg
import com.example.ui.theme.SurplusText
import com.example.viewmodel.CompVantageUiState

@Composable
fun KpiSummarySection(
    uiState: CompVantageUiState,
    modifier: Modifier = Modifier
) {
    // 4 Cards in responsive layout: 2x2 grid or column
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card 1: Gross Sales Eligible
            KpiCard(
                modifier = Modifier.weight(1f).testTag("kpi_card_gross_sales"),
                title = "Total Gross Sales (Eligible)",
                icon = Icons.Default.TrendingUp,
                iconTint = RoyalBlue,
                value = formatIndianCurrency(uiState.totalGrossSales),
                footer = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .background(SecondaryFixed.copy(alpha = 0.5f), RoundedCornerShape(3.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "+14.2% vs Q2 Target",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = EmeraldGreen
                                )
                            }
                        }
                        Text(
                            text = "Target: ${formatCrLakhs(uiState.totalTarget)}",
                            style = MetricMonoSmall.copy(fontSize = 11.sp),
                            color = Outline
                        )
                    }
                }
            )

            // Card 2: Total Incentive Calculated
            KpiCard(
                modifier = Modifier.weight(1f).testTag("kpi_card_total_incentive"),
                title = "Total Incentive Calculated",
                icon = Icons.Default.Payments,
                iconTint = PrimaryContainer,
                value = formatIndianCurrency(uiState.totalIncentiveCalculated),
                valueColor = RoyalBlue,
                footer = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "TDS 10% (194H):",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = Outline
                            )
                            Text(
                                text = formatIndianCurrency(uiState.totalTdsWithholding),
                                style = MetricMonoSmall.copy(fontSize = 11.sp),
                                color = ErrorRed
                            )
                        }
                        Text(
                            text = if (uiState.isPayoutApproved) "NEFT Approved" else "NEFT Ready",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = EmeraldGreen
                        )
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card 3: Team Target Achievement
            KpiCard(
                modifier = Modifier.weight(1f).testTag("kpi_card_achievement"),
                title = "Team Target Achievement",
                icon = Icons.Default.WorkspacePremium,
                iconTint = EmeraldGreen,
                value = String.format("%.1f%%", uiState.teamAchievementPercent),
                badge = "+8.4% Surplus",
                footer = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${uiState.qualifiedCount} of ${uiState.salesReps.size} Sales Reps Qualified",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Outline
                            )
                            Text(
                                text = "75% Quota Met",
                                style = MetricMonoSmall.copy(fontSize = 10.sp),
                                color = RoyalBlue
                            )
                        }
                        LinearProgressIndicator(
                            progress = { 0.75f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = EmeraldGreen,
                            trackColor = SurfaceContainer
                        )
                    }
                }
            )

            // Card 4: Surplus Rolled to Q4
            KpiCard(
                modifier = Modifier.weight(1f).testTag("kpi_card_surplus_rolled"),
                title = "Surplus Rolled to Q4",
                icon = Icons.Default.FastForward,
                iconTint = RoyalBlue,
                value = formatIndianCurrency(uiState.totalSurplusRolled),
                valueColor = RoyalBlue,
                footer = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Autorenew,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Forwarded to Jan-Mar '25",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(RoyalBlue.copy(alpha = 0.1f), RoundedCornerShape(3.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Auto-Rollover",
                                style = MetricMonoSmall.copy(fontSize = 9.sp),
                                color = RoyalBlue
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    badge: String? = null,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = Outline
                    )
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = value,
                        style = MetricMonoLarge,
                        color = valueColor
                    )
                    if (badge != null) {
                        Text(
                            text = badge,
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldGreen,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(OutlineVariant.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.height(8.dp))

            footer()
        }
    }
}
