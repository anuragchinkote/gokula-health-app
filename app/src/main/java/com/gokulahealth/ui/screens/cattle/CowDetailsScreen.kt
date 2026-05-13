package com.gokulahealth.ui.screens.cattle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.ui.components.TimelineItem
import com.gokulahealth.viewmodel.CowDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CowDetailsScreen(
    onNavigateBack: () -> Unit,
    viewModel: CowDetailsViewModel = hiltViewModel()
) {
    val cow by viewModel.cow.collectAsState()
    val milkEntries by viewModel.milkEntries.collectAsState()
    val vaccinations by viewModel.vaccinations.collectAsState()
    val heatCycles by viewModel.heatCycles.collectAsState()
    val treatments by viewModel.treatments.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Cattle") },
            text = { Text("Are you sure you want to delete this cattle profile? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteCow()
                    showDeleteDialog = false
                    onNavigateBack()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cow?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        cow?.let { details ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = details.imageUri ?: "https://via.placeholder.com/400",
                    contentDescription = details.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )

                InfoSection(details)

                SummarySection(milkEntries.sumOf { it.totalYield })

                Text(
                    text = "Health & Activity Timeline",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Combine all events for the timeline
                val events = remember(vaccinations, heatCycles, treatments) {
                    val all = mutableListOf<TimelineEvent>()
                    vaccinations.forEach { 
                        all.add(TimelineEvent(it.vaccineName, "Vaccination administered", it.vaccinationDate, Color(0xFFFF9800)))
                    }
                    heatCycles.forEach {
                        all.add(TimelineEvent("Heat Cycle", if (it.isPregnant) "Confirmed Pregnant" else "Observed in heat", it.cycleDate, Color(0xFFE91E63)))
                    }
                    treatments.forEach {
                        all.add(TimelineEvent(it.illness, "Treatment: ${it.medication}", it.date, Color(0xFFF44336)))
                    }
                    all.sortByDescending { it.date }
                    all
                }

                if (events.isEmpty()) {
                    Text("No history recorded yet.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    events.forEachIndexed { index, event ->
                        TimelineItem(
                            title = event.title,
                            subtitle = event.subtitle,
                            date = event.date,
                            iconColor = event.color,
                            isLast = index == events.size - 1
                        )
                    }
                }
            }
        }
    }
}

data class TimelineEvent(
    val title: String,
    val subtitle: String,
    val date: Long,
    val color: Color
)

@Composable
fun InfoSection(cow: CowEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoItem("Tag ID", cow.earTagId)
                InfoItem("Breed", cow.breed)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoItem("Age", "${cow.age} Years")
                InfoItem("Weight", "${cow.weight} Kg")
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SummarySection(totalMilk: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Lifetime Milk Dairy", style = MaterialTheme.typography.labelMedium)
                Text(text = "${String.format("%.1f", totalMilk)} Liters", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}
