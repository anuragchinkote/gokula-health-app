package com.gokulahealth.ui.screens.vaccination

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
import com.gokulahealth.data.local.entity.VaccinationEntity
import com.gokulahealth.ui.components.EmptyStateView
import com.gokulahealth.ui.components.PremiumButton
import com.gokulahealth.ui.components.PremiumTopBar
import com.gokulahealth.viewmodel.VaccinationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VaccinationScreen(
    onNavigateBack: () -> Unit,
    viewModel: VaccinationViewModel = hiltViewModel()
) {
    val upcomingVaccinations by viewModel.upcomingVaccinations.collectAsState()
    val cattleList by viewModel.cattleList.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PremiumTopBar(
                title = "Vaccinations",
                subtitle = "Health & immunity schedule",
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
                Icon(Icons.Default.Add, contentDescription = "Add Schedule")
            }
        }
    ) { padding ->
        if (upcomingVaccinations.isEmpty()) {
            EmptyStateView(
                message = "All my cattle are safe",
                subMessage = "No upcoming vaccinations scheduled at the moment."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(upcomingVaccinations) { vaccination ->
                    val cow = cattleList.find { it.id == vaccination.cowId }
                    VaccinationPremiumItem(vaccination, cow?.name ?: "Unknown")
                }
            }
        }
    }

    if (showAddDialog) {
        AddVaccinationPremiumDialog(
            cattleList = cattleList,
            onDismiss = { showAddDialog = false },
            onConfirm = { cowId, name, date, nextDate ->
                viewModel.addVaccination(cowId, name, date, nextDate)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun VaccinationPremiumItem(vaccination: VaccinationEntity, cowName: String) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val isOverdue = vaccination.nextDueDate < System.currentTimeMillis()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (isOverdue) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f) 
                             else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = vaccination.vaccineName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "My Cattle: $cowName", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Next Due: ${dateFormat.format(Date(vaccination.nextDueDate))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    fontWeight = if (isOverdue) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccinationPremiumDialog(
    cattleList: List<CowEntity>,
    onDismiss: () -> Unit,
    onConfirm: (Long, String, Long, Long) -> Unit
) {
    var selectedCow by remember { mutableStateOf(cattleList.firstOrNull()) }
    var vaccineName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val nextDueDate = Calendar.getInstance().apply { add(Calendar.MONTH, 6) }.timeInMillis

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Schedule Vaccination", fontWeight = FontWeight.Bold) },
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

                OutlinedTextField(
                    value = vaccineName,
                    onValueChange = { vaccineName = it },
                    label = { Text("Vaccine Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                
                Text(
                    text = "Reminder will be set for 6 months from now.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )

                PremiumButton(
                    text = "Schedule Now",
                    onClick = {
                        selectedCow?.let {
                            onConfirm(it.id, vaccineName, System.currentTimeMillis(), nextDueDate)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
