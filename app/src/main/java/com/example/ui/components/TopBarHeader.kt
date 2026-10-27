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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.PrimaryFixed
import com.example.ui.theme.PrimaryFixedDim
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.viewmodel.CompVantageUiState

@Composable
fun TopBarHeader(
    uiState: CompVantageUiState,
    onOpenNav: () -> Unit,
    onSearchChange: (String) -> Unit,
    onSelectQuarter: (String) -> Unit,
    onExportNeft: () -> Unit,
    onOpenAuditLogs: () -> Unit,
    onOpenNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quarterMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceContainerLowest)
            .border(1.dp, OutlineVariant.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Hamburger Menu (for mobile/tablet) + Breadcrumbs
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onOpenNav,
                    modifier = Modifier.testTag("nav_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Navigation Menu",
                        tint = RoyalBlue
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Compensation",
                            style = MaterialTheme.typography.labelSmall,
                            color = Outline
                        )
                        Text(
                            text = "/",
                            style = MaterialTheme.typography.labelSmall,
                            color = OutlineVariant
                        )
                        Text(
                            text = "FY 2024-25 Q3",
                            style = MaterialTheme.typography.labelSmall,
                            color = RoyalBlue
                        )
                        Text(
                            text = "/",
                            style = MaterialTheme.typography.labelSmall,
                            color = OutlineVariant
                        )
                        Box(
                            modifier = Modifier
                                .background(SurfaceContainer, RoundedCornerShape(4.dp))
                                .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Run #34 Live",
                                style = MetricMonoSmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Right side cluster: Mode badge, Quarter picker, Actions & User avatar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Live Net Mode Indicator
                Box(
                    modifier = Modifier
                        .background(SurfaceContainer, RoundedCornerShape(4.dp))
                        .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(EmeraldGreen)
                        )
                        Text(
                            text = "INR (₹) Net Mode",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "|",
                            style = MaterialTheme.typography.labelSmall,
                            color = Outline
                        )
                        Text(
                            text = "Live Grid Sync",
                            style = MetricMonoSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Quarter Selector Dropdown Button
                Box {
                    Box(
                        modifier = Modifier
                            .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                            .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                            .clickable { quarterMenuExpanded = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("quarter_selector_button")
                    ) {
                        Text(
                            text = uiState.selectedQuarter,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    DropdownMenu(
                        expanded = quarterMenuExpanded,
                        onDismissRequest = { quarterMenuExpanded = false }
                    ) {
                        listOf(
                            "Q3: Oct - Dec 2024 (Active Cycle)",
                            "Q2: Jul - Sep 2024 (Locked)",
                            "Q1: Apr - Jun 2024 (Audited)"
                        ).forEach { quarter ->
                            DropdownMenuItem(
                                text = { Text(quarter, style = MaterialTheme.typography.bodyMedium) },
                                onClick = {
                                    onSelectQuarter(quarter)
                                    quarterMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                // Export to Bank NEFT Button
                Box(
                    modifier = Modifier
                        .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                        .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                        .clickable { onExportNeft() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("export_neft_header_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Export Bank NEFT",
                            tint = RoyalBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Export to Bank NEFT",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Audit Formula Logs Button
                Box(
                    modifier = Modifier
                        .background(SurfaceContainerLowest, RoundedCornerShape(4.dp))
                        .border(1.dp, OutlineVariant, RoundedCornerShape(4.dp))
                        .clickable { onOpenAuditLogs() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("audit_logs_header_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HistoryEdu,
                            contentDescription = "Audit Formula Logs",
                            tint = Outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Audit Formula Logs",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Notification Bell Icon with Red Alert Indicator
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onOpenNotifications() }
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(ErrorRed)
                    )
                }

                // User Profile Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier
                        .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(PrimaryFixed)
                            .border(1.dp, PrimaryFixedDim, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RS",
                            style = MaterialTheme.typography.labelSmall,
                            color = RoyalBlue
                        )
                    }
                    Column {
                        Text(
                            text = "Rajesh Sharma",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "GM Sales Operations",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                            color = Outline
                        )
                    }
                }
            }
        }

        // Top Search Bar (Quick filter)
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchChange,
                placeholder = {
                    Text(
                        text = "Search Rep name, HSN Code, EMP-ID, or City...",
                        style = MaterialTheme.typography.labelMedium,
                        color = Outline
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Outline,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    Box(
                        modifier = Modifier
                            .background(SurfaceContainer, RoundedCornerShape(3.dp))
                            .border(1.dp, OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(3.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Ctrl + K",
                            style = MetricMonoSmall.copy(fontSize = 10.sp),
                            color = Outline
                        )
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoyalBlue,
                    unfocusedBorderColor = OutlineVariant,
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("top_search_input")
            )
        }
    }
}
