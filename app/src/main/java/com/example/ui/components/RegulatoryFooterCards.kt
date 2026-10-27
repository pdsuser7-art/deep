package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SurfaceContainerLowest

@Composable
fun RegulatoryFooterCards(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RegulatoryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.VerifiedUser,
            iconTint = RoyalBlue,
            title = "Income Tax 194H Deductions",
            description = "TDS at 10% is pre-computed on commission payouts exceeding ₹15,000 threshold for Indian residents."
        )

        RegulatoryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.SyncAlt,
            iconTint = EmeraldGreen,
            title = "Q4 Sales Rollover Policy",
            description = "Excess achievement above 100% quota is automatically added as base credit in Q4 target calculations."
        )

        RegulatoryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.HistoryToggleOff,
            iconTint = Outline,
            title = "Audit Trail & Formula Lock",
            description = "Run #34 hash timestamped on 28-Dec-2024. All variable modifications are logged in compliance logs."
        )
    }
}

@Composable
private fun RegulatoryCard(
    icon: ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(SurfaceContainerLowest, RoundedCornerShape(6.dp))
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier
                    .size(22.dp)
                    .padding(top = 2.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Outline
                )
            }
        }
    }
}
