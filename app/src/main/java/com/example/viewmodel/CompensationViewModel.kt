package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.AuditLogEntry
import com.example.model.ExcelLedgerFile
import com.example.model.FormulaConfig
import com.example.model.ProductHsn
import com.example.model.SalesRepresentative
import com.example.model.ScreenDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CompVantageUiState(
    val currentDestination: ScreenDestination = ScreenDestination.INCENTIVE_ENGINE,
    val selectedQuarter: String = "Q3: Oct - Dec 2024 (Active Cycle)",
    val formulaConfig: FormulaConfig = FormulaConfig(),
    val salesReps: List<SalesRepresentative> = emptyList(),
    val activeLedger: ExcelLedgerFile = ExcelLedgerFile(
        fileName = "Q3_North_West_Sales_Ledger_Dec2024.xlsx",
        sizeMb = 4.2,
        validatedRows = 3420,
        totalRows = 3420,
        sha256Check = "SHA-256 Verified",
        schemaColumns = listOf("Emp_ID", "Invoice_Date", "HSN_Code", "Net_Sales_Value (₹)"),
        uploadDate = "28-Dec-2024 14:22 IST"
    ),
    val searchQuery: String = "",
    val selectedRegion: String = "All Regions / Hubs",
    val selectedRole: String = "All Roles",
    val currentPage: Int = 1,
    val itemsPerPage: Int = 6,
    val isRecalculating: Boolean = false,
    val isPayoutApproved: Boolean = false,
    val showPayoutApprovalDialog: Boolean = false,
    val showAuditBreakdownRep: SalesRepresentative? = null,
    val showEditRepDialog: SalesRepresentative? = null,
    val showUploadDialog: Boolean = false,
    val showExportNeftDialog: Boolean = false,
    val showFormulaGuideDialog: Boolean = false,
    val showAuditLogsDialog: Boolean = false,
    val showNotificationsSheet: Boolean = false,
    val snackbarMessage: String? = null,
    val auditLogs: List<AuditLogEntry> = emptyList(),
    val hsnCatalog: List<ProductHsn> = emptyList()
) {
    val filteredReps: List<SalesRepresentative>
        get() {
            return salesReps.filter { rep ->
                val matchesSearch = searchQuery.isBlank() ||
                        rep.name.contains(searchQuery, ignoreCase = true) ||
                        rep.id.contains(searchQuery, ignoreCase = true) ||
                        rep.region.contains(searchQuery, ignoreCase = true)

                val matchesRegion = selectedRegion == "All Regions / Hubs" ||
                        rep.region.equals(selectedRegion.replace(" Region", "").trim(), ignoreCase = true) ||
                        (selectedRegion == "Delhi NCR" && rep.region == "Delhi NCR")

                val matchesRole = selectedRole == "All Roles" ||
                        rep.role.contains(selectedRole, ignoreCase = true)

                matchesSearch && matchesRegion && matchesRole
            }
        }

    val totalPages: Int
        get() {
            val count = filteredReps.size
            return if (count == 0) 1 else ((count - 1) / itemsPerPage) + 1
        }

    val paginatedReps: List<SalesRepresentative>
        get() {
            val list = filteredReps
            val startIndex = (currentPage - 1) * itemsPerPage
            if (startIndex >= list.size) return emptyList()
            val endIndex = minOf(startIndex + itemsPerPage, list.size)
            return list.subList(startIndex, endIndex)
        }

    // Dynamic aggregated totals based on active rules & data
    val totalTarget: Long
        get() = salesReps.sumOf { it.targetAmount }

    val totalGrossSales: Long
        get() = salesReps.sumOf { it.effectiveActual(formulaConfig.hsnExclusionActive) }

    val totalSurplusRolled: Long
        get() = salesReps.sumOf { it.surplusAmount(formulaConfig.autoForwardSurplus, formulaConfig.hsnExclusionActive) }

    val totalIncentiveCalculated: Long
        get() = salesReps.sumOf { it.calculateIncentive(formulaConfig.hsnExclusionActive) }

    val totalTdsWithholding: Long
        get() = salesReps.sumOf { it.calculateTds(formulaConfig.hsnExclusionActive) }

    val totalNetPayout: Long
        get() = salesReps.sumOf { it.calculateNetPayable(formulaConfig.hsnExclusionActive) }

    val teamAchievementPercent: Double
        get() = if (totalTarget > 0) (totalGrossSales.toDouble() / totalTarget.toDouble()) * 100.0 else 0.0

    val qualifiedCount: Int
        get() = salesReps.count { it.achievementPercent(formulaConfig.hsnExclusionActive) >= 100.0 }
}

class CompensationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CompVantageUiState())
    val uiState: StateFlow<CompVantageUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val initialReps = listOf(
            // Page 1 (The exact representatives from the prompt specification)
            SalesRepresentative(
                id = "EMP-1021",
                name = "Vikram Malhotra",
                initials = "VM",
                role = "Sr. Enterprise Sales Mgr",
                region = "Mumbai",
                targetAmount = 5000000L,
                actualSales = 6200000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1045",
                name = "Priya Sundaram",
                initials = "PS",
                role = "Key Account Exec",
                region = "Bengaluru",
                targetAmount = 3500000L,
                actualSales = 4120000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1088",
                name = "Amit Verma",
                initials = "AV",
                role = "Area Sales Officer",
                region = "Delhi NCR",
                targetAmount = 3000000L,
                actualSales = 3450000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1102",
                name = "Rajeshwari Patel",
                initials = "RP",
                role = "Territory Lead",
                region = "Ahmedabad",
                targetAmount = 2500000L,
                actualSales = 2880000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1130",
                name = "Sandeep Nair",
                initials = "SN",
                role = "Sales Officer",
                region = "Pune",
                targetAmount = 2000000L,
                actualSales = 1840000L,
                status = "Review Pending"
            ),
            SalesRepresentative(
                id = "EMP-1149",
                name = "Gurpreet Singh",
                initials = "GS",
                role = "Business Dev Exec",
                region = "Chandigarh",
                targetAmount = 2500000L,
                actualSales = 3600000L,
                status = "Approved"
            ),

            // Page 2 Representatives
            SalesRepresentative(
                id = "EMP-1165",
                name = "Ananya Sen",
                initials = "AS",
                role = "Sr. Enterprise Sales Mgr",
                region = "Kolkata",
                targetAmount = 3000000L,
                actualSales = 3300000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1180",
                name = "Karthik Raman",
                initials = "KR",
                role = "Key Account Exec",
                region = "Chennai",
                targetAmount = 2800000L,
                actualSales = 3100000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1194",
                name = "Neha Deshmukh",
                initials = "ND",
                role = "Territory Lead",
                region = "Pune",
                targetAmount = 2200000L,
                actualSales = 2450000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1205",
                name = "Rohit Bhatnagar",
                initials = "RB",
                role = "Area Sales Officer",
                region = "Delhi NCR",
                targetAmount = 2600000L,
                actualSales = 2780000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1218",
                name = "Divya Mehra",
                initials = "DM",
                role = "Business Dev Exec",
                region = "Mumbai",
                targetAmount = 1800000L,
                actualSales = 1680000L,
                status = "Review Pending"
            ),
            SalesRepresentative(
                id = "EMP-1232",
                name = "Manish Joshi",
                initials = "MJ",
                role = "Key Account Exec",
                region = "Ahmedabad",
                targetAmount = 2400000L,
                actualSales = 2520000L,
                status = "Approved"
            ),

            // Page 3 Representatives
            SalesRepresentative(
                id = "EMP-1245",
                name = "Sunita Rao",
                initials = "SR",
                role = "Sr. Enterprise Sales Mgr",
                region = "Bengaluru",
                targetAmount = 3200000L,
                actualSales = 3580000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1260",
                name = "Aditya Kulkarni",
                initials = "AK",
                role = "Sales Officer",
                region = "Mumbai",
                targetAmount = 1900000L,
                actualSales = 2100000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1277",
                name = "Pooja Banerjee",
                initials = "PB",
                role = "Business Dev Exec",
                region = "Kolkata",
                targetAmount = 1700000L,
                actualSales = 1520000L,
                status = "Review Pending"
            ),
            SalesRepresentative(
                id = "EMP-1289",
                name = "Vivek Chawla",
                initials = "VC",
                role = "Territory Lead",
                region = "Chandigarh",
                targetAmount = 2100000L,
                actualSales = 2310000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1302",
                name = "Deepak Reddy",
                initials = "DR",
                role = "Area Sales Officer",
                region = "Bengaluru",
                targetAmount = 2300000L,
                actualSales = 2480000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1315",
                name = "Meera Swaminathan",
                initials = "MS",
                role = "Key Account Exec",
                region = "Chennai",
                targetAmount = 2500000L,
                actualSales = 2720000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1328",
                name = "Tarun Kapoor",
                initials = "TK",
                role = "Sales Officer",
                region = "Delhi NCR",
                targetAmount = 1500000L,
                actualSales = 1350000L,
                status = "Review Pending"
            ),
            SalesRepresentative(
                id = "EMP-1339",
                name = "Shalini Singhania",
                initials = "SS",
                role = "Business Dev Exec",
                region = "Mumbai",
                targetAmount = 2000000L,
                actualSales = 2220000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1350",
                name = "Harish Pillai",
                initials = "HP",
                role = "Territory Lead",
                region = "Pune",
                targetAmount = 1800000L,
                actualSales = 1950000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1364",
                name = "Geeta Goswami",
                initials = "GG",
                role = "Sales Officer",
                region = "Ahmedabad",
                targetAmount = 1600000L,
                actualSales = 1750000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1378",
                name = "Naveen Saxena",
                initials = "NS",
                role = "Area Sales Officer",
                region = "Chandigarh",
                targetAmount = 1900000L,
                actualSales = 2050000L,
                status = "Approved"
            ),
            SalesRepresentative(
                id = "EMP-1390",
                name = "Farhan Qureshi",
                initials = "FQ",
                role = "Key Account Exec",
                region = "Bengaluru",
                targetAmount = 2200000L,
                actualSales = 2380000L,
                status = "Approved"
            )
        )

        val initialAuditLogs = listOf(
            AuditLogEntry(
                runId = "RUN-34-LIVE",
                timestamp = "28-Dec-2024 14:22:10 IST",
                operator = "Rajesh Sharma (GM)",
                action = "Batch Ledger Verification",
                details = "Validated 3,420 invoice rows against GSTIN 27AABCV8910J1Z4. SHA-256: d8f39...c4b",
                batchHash = "SHA256:7b1e4c8d90f23a"
            ),
            AuditLogEntry(
                runId = "RUN-33-AUDIT",
                timestamp = "27-Dec-2024 19:15:02 IST",
                operator = "System Automation",
                action = "TDS 194H Pre-Computation",
                details = "Computed Section 194H deductions at 10% for 18 reps crossing ₹15,000 threshold.",
                batchHash = "SHA256:3a91e0bc8722ff"
            ),
            AuditLogEntry(
                runId = "RUN-32-CONFIG",
                timestamp = "26-Dec-2024 11:40:45 IST",
                operator = "Rajesh Sharma (GM)",
                action = "Q4 Rollover Policy Locked",
                details = "Enabled Auto-Forward Surplus policy for Tier 2 and Tier 3 qualified reps.",
                batchHash = "SHA256:69baf4100c5a21"
            )
        )

        val initialHsn = listOf(
            ProductHsn(
                hsnCode = "8471",
                description = "Automatic data processing machines & optical readers",
                category = "Enterprise Hardware",
                gstRatePercent = 18,
                isEligible = true,
                quarterlyVolume = "₹2.85 Cr"
            ),
            ProductHsn(
                hsnCode = "8517",
                description = "Telephone sets, smartphones & transmission apparatus",
                category = "Telecom Infrastructure",
                gstRatePercent = 18,
                isEligible = true,
                quarterlyVolume = "₹1.42 Cr"
            ),
            ProductHsn(
                hsnCode = "9983",
                description = "Other professional, technical and business services",
                category = "SLA & AMC Contracts",
                gstRatePercent = 18,
                isEligible = true,
                quarterlyVolume = "₹55.5 L"
            ),
            ProductHsn(
                hsnCode = "9965",
                description = "Freight transport services (Excluded from commission)",
                category = "Logistics Ancillary",
                gstRatePercent = 5,
                isEligible = false,
                quarterlyVolume = "₹18.2 L"
            )
        )

        _uiState.update {
            it.copy(
                salesReps = initialReps,
                auditLogs = initialAuditLogs,
                hsnCatalog = initialHsn
            )
        }
    }

    fun selectDestination(destination: ScreenDestination) {
        _uiState.update { it.copy(currentDestination = destination) }
    }

    fun selectQuarter(quarter: String) {
        _uiState.update { it.copy(selectedQuarter = quarter) }
        showSnackbar("Switched cycle to $quarter")
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query, currentPage = 1) }
    }

    fun updateSelectedRegion(region: String) {
        _uiState.update { it.copy(selectedRegion = region, currentPage = 1) }
    }

    fun updateSelectedRole(role: String) {
        _uiState.update { it.copy(selectedRole = role, currentPage = 1) }
    }

    fun setPage(page: Int) {
        _uiState.update { it.copy(currentPage = page.coerceIn(1, it.totalPages)) }
    }

    fun toggleAutoForwardSurplus(enabled: Boolean) {
        _uiState.update {
            it.copy(
                formulaConfig = it.formulaConfig.copy(autoForwardSurplus = enabled)
            )
        }
        showSnackbar(if (enabled) "Auto-Forward Surplus to Q4 active" else "Surplus Rollover disabled")
    }

    fun toggleHsnCodeExclusion(enabled: Boolean) {
        _uiState.update {
            it.copy(
                formulaConfig = it.formulaConfig.copy(hsnExclusionActive = enabled)
            )
        }
        showSnackbar(if (enabled) "HSN 8471/8517 exclusion active (Freight/Warranty excluded)" else "HSN filter removed")
    }

    fun recalculateBatch() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRecalculating = true) }
            delay(750) // Micro-interaction feedback
            _uiState.update {
                it.copy(
                    isRecalculating = false,
                    snackbarMessage = "Batch #34 recalculated successfully with active Indian tax rules"
                )
            }
        }
    }

    fun recalculateSingleRep(repId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isRecalculating = true) }
            delay(400)
            _uiState.update { state ->
                val updated = state.salesReps.map { rep ->
                    if (rep.id == repId) {
                        rep.copy(status = "Approved")
                    } else rep
                }
                state.copy(
                    salesReps = updated,
                    isRecalculating = false,
                    snackbarMessage = "Representative $repId calculation verified and status set to Approved"
                )
            }
        }
    }

    fun openPayoutApprovalDialog() {
        _uiState.update { it.copy(showPayoutApprovalDialog = true) }
    }

    fun closePayoutApprovalDialog() {
        _uiState.update { it.copy(showPayoutApprovalDialog = false) }
    }

    fun executePayoutApproval() {
        _uiState.update { state ->
            val updatedReps = state.salesReps.map { it.copy(status = "Approved") }
            val newLog = AuditLogEntry(
                runId = "PAYOUT-SIGN-OFF",
                timestamp = "Just now",
                operator = "Rajesh Sharma (GM Sales Ops)",
                action = "Payout Execution Approval",
                details = "Approved ₹18,45,200 total payouts across 18 qualified executives with TDS 194H withheld.",
                batchHash = "SHA256:99f4d1e20c3a"
            )
            state.copy(
                isPayoutApproved = true,
                showPayoutApprovalDialog = false,
                salesReps = updatedReps,
                auditLogs = listOf(newLog) + state.auditLogs,
                snackbarMessage = "Batch #34 payout executed and bank authorization package generated!"
            )
        }
    }

    fun openAuditBreakdown(rep: SalesRepresentative) {
        _uiState.update { it.copy(showAuditBreakdownRep = rep) }
    }

    fun closeAuditBreakdown() {
        _uiState.update { it.copy(showAuditBreakdownRep = null) }
    }

    fun openEditRepDialog(rep: SalesRepresentative) {
        _uiState.update { it.copy(showEditRepDialog = rep) }
    }

    fun closeEditRepDialog() {
        _uiState.update { it.copy(showEditRepDialog = null) }
    }

    fun saveRepAdjustment(repId: String, newTarget: Long, newActual: Long) {
        _uiState.update { state ->
            val updated = state.salesReps.map { rep ->
                if (rep.id == repId) {
                    rep.copy(targetAmount = newTarget, actualSales = newActual)
                } else rep
            }
            state.copy(
                salesReps = updated,
                showEditRepDialog = null,
                snackbarMessage = "Updated target and actuals for representative $repId"
            )
        }
    }

    fun openUploadDialog() {
        _uiState.update { it.copy(showUploadDialog = true) }
    }

    fun closeUploadDialog() {
        _uiState.update { it.copy(showUploadDialog = false) }
    }

    fun simulateFileUpload(newFileName: String, rowCount: Int) {
        _uiState.update { state ->
            val updatedLedger = state.activeLedger.copy(
                fileName = newFileName,
                validatedRows = rowCount,
                totalRows = rowCount,
                uploadDate = "Today, Just now"
            )
            state.copy(
                activeLedger = updatedLedger,
                showUploadDialog = false,
                snackbarMessage = "Successfully imported $rowCount rows from $newFileName"
            )
        }
    }

    fun openExportNeftDialog() {
        _uiState.update { it.copy(showExportNeftDialog = true) }
    }

    fun closeExportNeftDialog() {
        _uiState.update { it.copy(showExportNeftDialog = false) }
    }

    fun openFormulaGuideDialog() {
        _uiState.update { it.copy(showFormulaGuideDialog = true) }
    }

    fun closeFormulaGuideDialog() {
        _uiState.update { it.copy(showFormulaGuideDialog = false) }
    }

    fun openAuditLogsDialog() {
        _uiState.update { it.copy(showAuditLogsDialog = true) }
    }

    fun closeAuditLogsDialog() {
        _uiState.update { it.copy(showAuditLogsDialog = false) }
    }

    fun toggleNotificationsSheet(visible: Boolean) {
        _uiState.update { it.copy(showNotificationsSheet = visible) }
    }

    fun showSnackbar(message: String) {
        _uiState.update { it.copy(snackbarMessage = message) }
    }

    fun dismissSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}
