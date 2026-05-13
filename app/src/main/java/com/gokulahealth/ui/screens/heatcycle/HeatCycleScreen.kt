package com.gokulahealth.ui.screens.heatcycle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.local.entity.HeatCycleEntity
import com.gokulahealth.ui.components.EmptyStateView
import com.gokulahealth.ui.components.PremiumButton
import com.gokulahealth.ui.components.PremiumTopBar
import com.gokulahealth.viewmodel.HeatCycleViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HeatCycleScreen(
    onNavigateBack: () -> Unit,
    viewModel: HeatCycleViewModel = hiltViewModel()
) {
    val upcomingHeatCycles by viewModel.upcomingHeatCycles.collectAsState()
    val cattleList by viewModel.cattleList.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PremiumTopBar(
                title = "Heat Cycle",
                subtitle = "Track fertile periods & pregnancy",
                onBackClick = onNavigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Add, contentDescription = "Log Cycle")
            }
        }
    ) { padding ->
        if (upcomingHeatCycles.isEmpty()) {
            EmptyStateView(
                message = "No active cycles",
                subMessage = "Record heat cycles to predict fertile periods and pregnancy."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(upcomingHeatCycles) { cycle ->
                    val cow = cattleList.find { it.id == cycle.cowId }
                    HeatCyclePremiumItem(cycle, cow?.name ?: "Unknown")
                }
            }
        }
    }

    if (showAddDialog) {
        AddHeatCyclePremiumDialog(
            cattleList = cattleList,
            onDismiss = { showAddDialog = false },
            onConfirm = { cowId, isPregnant ->
                viewModel.addHeatCycle(cowId, System.currentTimeMillis(), isPregnant)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun HeatCyclePremiumItem(cycle: HeatCycleEntity, cowName: String) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (cycle.isPregnant) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) 
                             else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cowName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Surface(
                    color = if (cycle.isPregnant) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) 
                            else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = if (cycle.isPregnant) "Status: Pregnant" else "Status: In Heat",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (cycle.isPregnant) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.tertiary
                    )
                }
                Text(
                    text = "Next Expected: ${dateFormat.format(Date(cycle.nextExpectedDate))}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHeatCyclePremiumDialog(
    cattleList: List<CowEntity>,
    onDismiss: () -> Unit,
    onConfirm: (Long, Boolean) -> Unit
) {
    var selectedCow by remember { mutableStateOf(cattleList.firstOrNull()) }
    var isPregnant by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Log Status", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedCow?.name ?: "Select My Cattle",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("My Cattle") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        cattleList.forEach { cow ->
                            DropdownMenuItem(
                                text = { Text(cow.name) },
                                onClick = {
                                    selectedCow = cow
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(checked = isPregnant, onCheckedChange = { isPregnant = it })
                    Text(text = "Mark as Pregnant", style = MaterialTheme.typography.bodyLarge)
                }

                PremiumButton(
                    text = "Save Record",
                    onClick = {
                        selectedCow?.let {
                            onConfirm(it.id, isPregnant)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
