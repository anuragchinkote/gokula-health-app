package com.gokulahealth.ui.screens.milk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.local.entity.MilkEntryEntity
import com.gokulahealth.ui.components.EmptyStateView
import com.gokulahealth.ui.components.MilkAnalyticsCard
import com.gokulahealth.ui.components.PremiumButton
import com.gokulahealth.ui.components.PremiumTopBar
import com.gokulahealth.viewmodel.MilkViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MilkDiaryScreen(
    onNavigateBack: () -> Unit,
    viewModel: MilkViewModel = hiltViewModel()
) {
    val cattleList by viewModel.cattleList.collectAsState()
    val milkHistory by viewModel.milkHistory.collectAsState()
    val selectedCowId by viewModel.selectedCowId.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PremiumTopBar(
                title = "Milk Diary",
                subtitle = "Daily collection logs",
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
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            
            // Stats Summary
            if (milkHistory.isNotEmpty()) {
                Box(modifier = Modifier.padding(16.dp)) {
                    MilkAnalyticsCard(
                        totalYield = "${String.format("%.1f", milkHistory.sumOf { it.totalYield })}L",
                        avgYield = "${String.format("%.1f", if(milkHistory.isNotEmpty()) milkHistory.sumOf { it.totalYield } / milkHistory.size else 0.0)}L",
                        highestYield = "${String.format("%.1f", milkHistory.maxOfOrNull { it.totalYield } ?: 0.0)}L"
                    )
                }
            }

            // Cow Filter
            CowFilterScrollable(
                cattleList = cattleList,
                selectedCowId = selectedCowId,
                onCowSelected = { viewModel.selectCow(it) }
            )

            if (milkHistory.isEmpty()) {
                EmptyStateView(
                    message = "No milk entries",
                    subMessage = "Start tracking daily yield by tapping the + button."
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(milkHistory) { entry ->
                        MilkEntryPremiumItem(entry, cattleList.find { it.id == entry.cowId }?.name ?: "Unknown")
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddMilkEntryDialog(
            cattleList = cattleList,
            onDismiss = { showAddDialog = false },
            onConfirm = { cowId, morning, evening ->
                viewModel.addMilkEntry(cowId, morning, evening)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CowFilterScrollable(
    cattleList: List<CowEntity>,
    selectedCowId: Long?,
    onCowSelected: (Long?) -> Unit
) {
    ScrollableTabRow(
        selectedTabIndex = if (selectedCowId == null) 0 else cattleList.indexOfFirst { it.id == selectedCowId } + 1,
        edgePadding = 16.dp,
        divider = {},
        containerColor = Color.Transparent,
        indicator = {}
    ) {
        FilterChip(
            selected = selectedCowId == null,
            onClick = { onCowSelected(null) },
            label = { Text("All My Cattle") },
            modifier = Modifier.padding(horizontal = 4.dp),
            shape = MaterialTheme.shapes.medium
        )
        cattleList.forEach { cow ->
            FilterChip(
                selected = selectedCowId == cow.id,
                onClick = { onCowSelected(cow.id) },
                label = { Text(cow.name) },
                modifier = Modifier.padding(horizontal = 4.dp),
                shape = MaterialTheme.shapes.medium
            )
        }
    }
}

@Composable
fun MilkEntryPremiumItem(entry: MilkEntryEntity, cowName: String) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cowName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = dateFormat.format(Date(entry.date)), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.totalYield} L", 
                    style = MaterialTheme.typography.titleLarge, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "M: ${entry.morningYield} | E: ${entry.eveningYield}", 
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMilkEntryDialog(
    cattleList: List<CowEntity>,
    onDismiss: () -> Unit,
    onConfirm: (Long, Double, Double) -> Unit
) {
    var selectedCow by remember { mutableStateOf(cattleList.firstOrNull()) }
    var morningYield by remember { mutableStateOf("") }
    var eveningYield by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        title = { Text("Log Yield", fontWeight = FontWeight.Bold) },
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

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = morningYield,
                        onValueChange = { morningYield = it },
                        label = { Text("Morning") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    )

                    OutlinedTextField(
                        value = eveningYield,
                        onValueChange = { eveningYield = it },
                        label = { Text("Evening") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = MaterialTheme.shapes.medium
                    )
                }

                PremiumButton(
                    text = "Save Entry",
                    onClick = {
                        selectedCow?.let {
                            onConfirm(it.id, morningYield.toDoubleOrNull() ?: 0.0, eveningYield.toDoubleOrNull() ?: 0.0)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}
