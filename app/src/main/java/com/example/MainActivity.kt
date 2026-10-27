package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ScreenDestination
import com.example.ui.components.AuditBreakdownDialog
import com.example.ui.components.AuditLogsDialog
import com.example.ui.components.EditRepDialog
import com.example.ui.components.ExcelUploadFormulaSection
import com.example.ui.components.ExportNeftDialog
import com.example.ui.components.FormulaGuideDialog
import com.example.ui.components.IncentiveDataGrid
import com.example.ui.components.KpiSummarySection
import com.example.ui.components.PayoutApprovalDialog
import com.example.ui.components.RegulatoryFooterCards
import com.example.ui.components.SideNavContent
import com.example.ui.components.TopBarHeader
import com.example.ui.components.UploadExcelDialog
import com.example.ui.screens.EmployeesAttendanceScreen
import com.example.ui.screens.ExcelUploadScreen
import com.example.ui.screens.HsnCatalogScreen
import com.example.ui.screens.PayoutReportsScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Outline
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SecondaryFixed
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.SurfaceTint
import com.example.viewmodel.CompensationViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CompVantageApp()
            }
        }
    }
}

@Composable
fun CompVantageApp(
    viewModel: CompensationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Display snackbar updates
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissSnackbar()
        }
    }

    // Handle back button for secondary screens
    BackHandler(enabled = uiState.currentDestination != ScreenDestination.INCENTIVE_ENGINE) {
        viewModel.selectDestination(ScreenDestination.INCENTIVE_ENGINE)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideNavContent(
                currentDestination = uiState.currentDestination,
                onSelectDestination = { dest ->
                    viewModel.selectDestination(dest)
                    scope.launch { drawerState.close() }
                },
                onProcessRunClick = {
                    viewModel.recalculateBatch()
                    scope.launch { drawerState.close() }
                },
                onHelpClick = {
                    viewModel.openFormulaGuideDialog()
                    scope.launch { drawerState.close() }
                },
                onSystemLogsClick = {
                    viewModel.openAuditLogsDialog()
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            contentWindowInsets = WindowInsets.safeDrawing,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Persistent Top Bar
                TopBarHeader(
                    uiState = uiState,
                    onOpenNav = { scope.launch { drawerState.open() } },
                    onSearchChange = { viewModel.updateSearchQuery(it) },
                    onSelectQuarter = { viewModel.selectQuarter(it) },
                    onExportNeft = { viewModel.openExportNeftDialog() },
                    onOpenAuditLogs = { viewModel.openAuditLogsDialog() },
                    onOpenNotifications = { viewModel.openAuditLogsDialog() }
                )

                // Main Content Canvas (Scrollable)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    when (uiState.currentDestination) {
                        ScreenDestination.INCENTIVE_ENGINE -> {
                            // 1. Canvas Header Bar with Process CTA
                            IncentiveEngineHeader(
                                isRecalculating = uiState.isRecalculating,
                                isPayoutApproved = uiState.isPayoutApproved,
                                onRecalculateClick = { viewModel.recalculateBatch() },
                                onExecutePayoutClick = { viewModel.openPayoutApprovalDialog() }
                            )

                            // 2. Top KPI Summary Cards (4 Cards)
                            KpiSummarySection(uiState = uiState)

                            // 3. Interactive Excel Upload & Formula Configuration
                            ExcelUploadFormulaSection(
                                uiState = uiState,
                                onToggleAutoForward = { viewModel.toggleAutoForwardSurplus(it) },
                                onToggleHsnExclusion = { viewModel.toggleHsnCodeExclusion(it) },
                                onOpenUploadDialog = { viewModel.openUploadDialog() },
                                onOpenFormulaGuide = { viewModel.openFormulaGuideDialog() }
                            )

                            // 4. Main High-Density Data Table
                            IncentiveDataGrid(
                                uiState = uiState,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                onRegionSelect = { viewModel.updateSelectedRegion(it) },
                                onRoleSelect = { viewModel.updateSelectedRole(it) },
                                onBulkRecalculate = { viewModel.recalculateBatch() },
                                onDownloadExcelReport = { viewModel.openExportNeftDialog() },
                                onAuditFormulaClick = { viewModel.openAuditBreakdown(it) },
                                onEditRepClick = { viewModel.openEditRepDialog(it) },
                                onRecalculateSingleRep = { viewModel.recalculateSingleRep(it) },
                                onPageSelect = { viewModel.setPage(it) }
                            )

                            // 5. Contextual Regulatory & Governance Notes
                            RegulatoryFooterCards()

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        ScreenDestination.DASHBOARD -> {
                            // Dashboard view with summary KPIs and quick actions
                            IncentiveEngineHeader(
                                isRecalculating = uiState.isRecalculating,
                                isPayoutApproved = uiState.isPayoutApproved,
                                onRecalculateClick = { viewModel.recalculateBatch() },
                                onExecutePayoutClick = { viewModel.openPayoutApprovalDialog() }
                            )
                            KpiSummarySection(uiState = uiState)
                            IncentiveDataGrid(
                                uiState = uiState,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                onRegionSelect = { viewModel.updateSelectedRegion(it) },
                                onRoleSelect = { viewModel.updateSelectedRole(it) },
                                onBulkRecalculate = { viewModel.recalculateBatch() },
                                onDownloadExcelReport = { viewModel.openExportNeftDialog() },
                                onAuditFormulaClick = { viewModel.openAuditBreakdown(it) },
                                onEditRepClick = { viewModel.openEditRepDialog(it) },
                                onRecalculateSingleRep = { viewModel.recalculateSingleRep(it) },
                                onPageSelect = { viewModel.setPage(it) }
                            )
                        }

                        ScreenDestination.EXCEL_UPLOAD -> {
                            ExcelUploadScreen(
                                uiState = uiState,
                                onOpenUploadDialog = { viewModel.openUploadDialog() }
                            )
                        }

                        ScreenDestination.EMPLOYEES -> {
                            EmployeesAttendanceScreen(uiState = uiState)
                        }

                        ScreenDestination.HSN_CATALOG -> {
                            HsnCatalogScreen(uiState = uiState)
                        }

                        ScreenDestination.PAYOUT_REPORTS -> {
                            PayoutReportsScreen(
                                uiState = uiState,
                                onExportNeft = { viewModel.openExportNeftDialog() }
                            )
                        }

                        ScreenDestination.SETTINGS -> {
                            SettingsScreen(
                                uiState = uiState,
                                onToggleAutoForward = { viewModel.toggleAutoForwardSurplus(it) },
                                onToggleHsnExclusion = { viewModel.toggleHsnCodeExclusion(it) }
                            )
                        }
                    }
                }
            }

            // Dialogs
            if (uiState.showPayoutApprovalDialog) {
                PayoutApprovalDialog(
                    qualifiedCount = uiState.qualifiedCount,
                    totalGrossSales = uiState.totalGrossSales,
                    totalIncentive = uiState.totalIncentiveCalculated,
                    totalTds = uiState.totalTdsWithholding,
                    onConfirm = { viewModel.executePayoutApproval() },
                    onDismiss = { viewModel.closePayoutApprovalDialog() }
                )
            }

            uiState.showAuditBreakdownRep?.let { rep ->
                AuditBreakdownDialog(
                    rep = rep,
                    autoForwardSurplus = uiState.formulaConfig.autoForwardSurplus,
                    hsnExclusionActive = uiState.formulaConfig.hsnExclusionActive,
                    onRecalculate = { viewModel.recalculateSingleRep(rep.id) },
                    onDismiss = { viewModel.closeAuditBreakdown() }
                )
            }

            uiState.showEditRepDialog?.let { rep ->
                EditRepDialog(
                    rep = rep,
                    onSave = { target, actual ->
                        viewModel.saveRepAdjustment(rep.id, target, actual)
                    },
                    onDismiss = { viewModel.closeEditRepDialog() }
                )
            }

            if (uiState.showUploadDialog) {
                UploadExcelDialog(
                    onUpload = { fileName, count ->
                        viewModel.simulateFileUpload(fileName, count)
                    },
                    onDismiss = { viewModel.closeUploadDialog() }
                )
            }

            if (uiState.showExportNeftDialog) {
                ExportNeftDialog(
                    reps = uiState.salesReps,
                    hsnExclusionActive = uiState.formulaConfig.hsnExclusionActive,
                    onDismiss = { viewModel.closeExportNeftDialog() }
                )
            }

            if (uiState.showFormulaGuideDialog) {
                FormulaGuideDialog(
                    onDismiss = { viewModel.closeFormulaGuideDialog() }
                )
            }

            if (uiState.showAuditLogsDialog) {
                AuditLogsDialog(
                    logs = uiState.auditLogs,
                    onDismiss = { viewModel.closeAuditLogsDialog() }
                )
            }
        }
    }
}

@Composable
private fun IncentiveEngineHeader(
    isRecalculating: Boolean,
    isPayoutApproved: Boolean,
    onRecalculateClick: () -> Unit,
    onExecutePayoutClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spin_header")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(800, easing = LinearEasing)),
        label = "rotation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 0.dp,
                color = Color.Transparent
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Incentive Engine & Calculation Grid",
                        style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Box(
                        modifier = Modifier
                            .background(SecondaryFixed, RoundedCornerShape(4.dp))
                            .border(1.dp, EmeraldGreen.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "FY 2024-25 Q3 Final Batch",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldGreen
                            )
                        }
                    }
                }
                Text(
                    text = "Automated commission processing with intelligent sales rollover across multi-tier slabs and TDS 194H withholding calculations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // CTAs: Recalculate Batch & Execute Payout Approval
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onRecalculateClick,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SurfaceContainerLowest,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("recalculate_batch_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Recalculate",
                        tint = Outline,
                        modifier = Modifier
                            .size(16.dp)
                            .then(if (isRecalculating) Modifier.rotate(spinAngle) else Modifier)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Recalculate Batch",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Button(
                    onClick = onExecutePayoutClick,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPayoutApproved) EmeraldGreen else RoyalBlue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("execute_payout_button")
                ) {
                    Icon(
                        imageVector = if (isPayoutApproved) Icons.Default.CheckCircle else Icons.Default.Bolt,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPayoutApproved) "Payout Approved" else "Execute Payout Approval",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}
