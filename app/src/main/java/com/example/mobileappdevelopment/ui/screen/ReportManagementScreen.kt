package com.example.mobileappdevelopment.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobileappdevelopment.data.Report
import com.example.mobileappdevelopment.data.ReportPriority
import com.example.mobileappdevelopment.data.ReportStatus
import com.example.mobileappdevelopment.veiwmodel.ReportViewModel

@Composable
fun ReportManagementScreen(
    viewModel: ReportViewModel
) {
    val reports by viewModel.reports.collectAsState()
    val filterStatus by viewModel.filterStatus.collectAsState()

    var selectedReport by remember { mutableStateOf<Report?>(null) }

    val filteredReports = remember(reports, filterStatus) {
        if (filterStatus == null) {
            reports
        } else {
            reports.filter { it.status == filterStatus }
        }
    }

    val reportsByStatus = remember(reports) {
        mapOf(
            null to reports.size,
            ReportStatus.PENDING to reports.count { it.status == ReportStatus.PENDING },
            ReportStatus.INVESTIGATING to reports.count { it.status == ReportStatus.INVESTIGATING },
            ReportStatus.RESOLVED to reports.count { it.status == ReportStatus.RESOLVED },
            ReportStatus.CLOSED to reports.count { it.status == ReportStatus.CLOSED }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Report Status",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier
            .padding(4.dp)
        )
        Card {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Manage all anonymous reports received.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusCard(
                        label = "All",
                        count = reportsByStatus[null] ?: 0,
                        onClick = { viewModel.setFilterStatus(null) },
                        modifier = Modifier.weight(1f)
                    )
                    StatusCard(
                        label = "Pending",
                        count = reportsByStatus[ReportStatus.PENDING] ?: 0,
                        onClick = { viewModel.setFilterStatus(ReportStatus.PENDING) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatusCard(
                        label = "Investigating",
                        count = reportsByStatus[ReportStatus.INVESTIGATING] ?: 0,
                        onClick = { viewModel.setFilterStatus(ReportStatus.INVESTIGATING) },
                        modifier = Modifier.weight(1f)
                    )
                    StatusCard(
                        label = "Resolved",
                        count = reportsByStatus[ReportStatus.RESOLVED] ?: 0,
                        onClick = { viewModel.setFilterStatus(ReportStatus.RESOLVED) },
                        modifier = Modifier.weight(1f)
                    )
                    StatusCard(
                        label = "Closed",
                        count = reportsByStatus[ReportStatus.CLOSED] ?: 0,
                        onClick = { viewModel.setFilterStatus(ReportStatus.CLOSED) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filteredReports.forEach { report ->
                        ReportCard(
                            report = report,
                            onClick = { selectedReport = report }
                        )
                    }

                    if (filteredReports.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No reports found for this category.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    selectedReport?.let { report ->
        ReportDetailDialog(
            report = report,
            onDismiss = { selectedReport = null },
            onUpdateStatus = { status -> viewModel.updateReportStatus(report.id, status) },
            onUpdatePriority = { priority -> viewModel.updateReportPriority(report.id, priority) },
            onUpdateNotes = { notes -> viewModel.updateReportNotes(report.id, notes) }
        )
    }
}

@Composable
fun StatusCard(
    label: String,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun ReportCard(
    report: Report,
    onClick: () -> Unit
) {
    Card(onClick = onClick) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.title ?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                
                AssistChip(
                    onClick = {},
                    label = { Text(report.status?.label ?: "No Status", style = MaterialTheme.typography.labelSmall) }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(report.priority?.label ?: "No Priority", style = MaterialTheme.typography.labelSmall) }
                )
                AssistChip(
                    onClick = {},
                    label = { Text(report.category?.label ?: "No Category", style = MaterialTheme.typography.labelSmall) }
                )
            }

            Text(
                text = report.description ?: "No Description",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                
                if (!report.department.isNullOrBlank()) {
                    Text(
                        text = report.department,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(" • ", color = MaterialTheme.colorScheme.onSurfaceVariant) 
                }
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = "Date",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Text(
                    text = report.date ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailDialog(
    report: Report,
    onDismiss: () -> Unit,
    onUpdateStatus: (ReportStatus) -> Unit,
    onUpdatePriority: (ReportPriority) -> Unit,
    onUpdateNotes: (String) -> Unit
) {
    
    var notes by remember { mutableStateOf(report.notes ?: "") }
    var expandedStatus by remember { mutableStateOf(false) }
    var expandedPriority by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Report Details") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Report ID: ${report.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Title", style = MaterialTheme.typography.labelMedium)
                    
                    Text(report.title ?: "No Title", style = MaterialTheme.typography.bodyMedium)
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Report Type", style = MaterialTheme.typography.labelMedium)
                    AssistChip(
                        onClick = {},
                        
                        label = { Text(report.category?.label ?: "No Category") }
                    )
                }

                
                if (!report.department.isNullOrBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Related Department", style = MaterialTheme.typography.labelMedium)
                        Text(report.department, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                
                if (!report.date.isNullOrBlank()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Date of Occurrence", style = MaterialTheme.typography.labelMedium)
                        Text(report.date, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Details", style = MaterialTheme.typography.labelMedium)
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            
                            text = report.description ?: "No details provided.",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Status", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedStatus,
                            onExpandedChange = { expandedStatus = it }
                        ) {
                            OutlinedTextField(
                                
                                value = report.status?.label ?: "No Status",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedStatus,
                                onDismissRequest = { expandedStatus = false }
                            ) {
                                ReportStatus.values().forEach { status ->
                                    DropdownMenuItem(
                                        text = { Text(status.label) },
                                        onClick = {
                                            onUpdateStatus(status)
                                            expandedStatus = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Priority", style = MaterialTheme.typography.labelMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        ExposedDropdownMenuBox(
                            expanded = expandedPriority,
                            onExpandedChange = { expandedPriority = it }
                        ) {
                            OutlinedTextField(
                                
                                value = report.priority?.label ?: "No Priority",
                                onValueChange = {},
                                textStyle = MaterialTheme.typography.bodySmall,
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPriority) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedPriority,
                                onDismissRequest = { expandedPriority = false }
                            ) {
                                ReportPriority.values().forEach { priority ->
                                    DropdownMenuItem(
                                        text = { Text(priority.label) },
                                        onClick = {
                                            onUpdatePriority(priority)
                                            expandedPriority = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Admin Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdateNotes(notes)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
