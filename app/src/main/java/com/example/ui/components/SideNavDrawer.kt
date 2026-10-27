package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenDestination
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MetricMonoSmall
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest

@Composable
fun SideNavContent(
    currentDestination: ScreenDestination,
    onSelectDestination: (ScreenDestination) -> Unit,
    onProcessRunClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSystemLogsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(SurfaceContainerLowest)
            .border(1.dp, OutlineVariant.copy(alpha = 0.5f))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Upper Section: Logo & Nav items
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Brand Logo Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(RoyalBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = "CompVantage IN Logo",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = "CompVantage IN",
                        style = MaterialTheme.typography.headlineSmall.copy(fontSize = 17.sp),
                        color = RoyalBlue
                    )
                    Text(
                        text = "Enterprise Compensation",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Outline
                    )
                }
            }

            // Compliance Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLow, RoundedCornerShape(4.dp))
                    .border(1.dp, OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            text = "TDS 194H Compliant",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "IN-SPM",
                        style = MetricMonoSmall.copy(fontSize = 10.sp),
                        color = RoyalBlue
                    )
                }
            }

            // Navigation Links
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                NavItemRow(
                    destination = ScreenDestination.DASHBOARD,
                    icon = Icons.Default.Dashboard,
                    label = "Dashboard",
                    isSelected = currentDestination == ScreenDestination.DASHBOARD,
                    onClick = { onSelectDestination(ScreenDestination.DASHBOARD) }
                )
                NavItemRow(
                    destination = ScreenDestination.EXCEL_UPLOAD,
                    icon = Icons.Default.UploadFile,
                    label = "Excel Sales Upload",
                    isSelected = currentDestination == ScreenDestination.EXCEL_UPLOAD,
                    onClick = { onSelectDestination(ScreenDestination.EXCEL_UPLOAD) }
                )
                NavItemRow(
                    destination = ScreenDestination.INCENTIVE_ENGINE,
                    icon = Icons.Default.Calculate,
                    label = "Incentive Engine",
                    badge = "Active",
                    isSelected = currentDestination == ScreenDestination.INCENTIVE_ENGINE,
                    onClick = { onSelectDestination(ScreenDestination.INCENTIVE_ENGINE) }
                )
                NavItemRow(
                    destination = ScreenDestination.EMPLOYEES,
                    icon = Icons.Default.Badge,
                    label = "Employees & Attendance",
                    isSelected = currentDestination == ScreenDestination.EMPLOYEES,
                    onClick = { onSelectDestination(ScreenDestination.EMPLOYEES) }
                )
                NavItemRow(
                    destination = ScreenDestination.HSN_CATALOG,
                    icon = Icons.Default.Inventory2,
                    label = "Product & HSN Catalog",
                    isSelected = currentDestination == ScreenDestination.HSN_CATALOG,
                    onClick = { onSelectDestination(ScreenDestination.HSN_CATALOG) }
                )
                NavItemRow(
                    destination = ScreenDestination.PAYOUT_REPORTS,
                    icon = Icons.Default.Payments,
                    label = "Payout Reports",
                    isSelected = currentDestination == ScreenDestination.PAYOUT_REPORTS,
                    onClick = { onSelectDestination(ScreenDestination.PAYOUT_REPORTS) }
                )
                NavItemRow(
                    destination = ScreenDestination.SETTINGS,
                    icon = Icons.Default.Settings,
                    label = "Settings",
                    isSelected = currentDestination == ScreenDestination.SETTINGS,
                    onClick = { onSelectDestination(ScreenDestination.SETTINGS) }
                )
            }
        }

        // Bottom Actions & System State
        Column(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Primary Action: Process Q3 Run
            Button(
                onClick = onProcessRunClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryContainer,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("process_q3_run_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Process Q3 Run",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Help & Regulatory Docs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHelpClick() }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.HelpCenter,
                    contentDescription = "Help Docs",
                    tint = Outline,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Help & Regulatory Docs",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // System Logs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSystemLogsClick() }
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = "System Logs",
                        tint = Outline,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "System Logs",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(EmeraldGreen)
                )
            }

            // GSTIN status footer chip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainerLow.copy(alpha = 0.7f), RoundedCornerShape(4.dp))
                    .border(1.dp, OutlineVariant.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "GSTIN: 27AABCV8910J1Z4",
                        style = MetricMonoSmall.copy(fontSize = 10.sp),
                        color = Outline
                    )
                    Text(
                        text = "Synced",
                        style = MetricMonoSmall.copy(fontSize = 10.sp),
                        color = EmeraldGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun NavItemRow(
    destination: ScreenDestination,
    icon: ImageVector,
    label: String,
    badge: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) SurfaceContainerLow else Color.Transparent
    val textColor = if (isSelected) RoyalBlue else MaterialTheme.colorScheme.onSurfaceVariant
    val iconTint = if (isSelected) RoyalBlue else Outline

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(
                    width = 2.dp,
                    color = RoyalBlue,
                    shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 4.dp, bottomEnd = 4.dp)
                ) else Modifier
            )
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .testTag("nav_item_${destination.name.lowercase()}"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .background(PrimaryContainer, RoundedCornerShape(3.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badge,
                    style = MetricMonoSmall.copy(fontSize = 9.sp),
                    color = Color.White
                )
            }
        }
    }
}
