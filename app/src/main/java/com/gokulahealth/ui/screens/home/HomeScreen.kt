package com.gokulahealth.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gokulahealth.ui.components.PremiumDashboardCard
import com.gokulahealth.ui.components.PremiumStatCard
import com.gokulahealth.ui.components.PremiumTopBar
import com.gokulahealth.ui.theme.AgriGradients
import com.gokulahealth.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAddCow: () -> Unit,
    onNavigateToMilkDiary: () -> Unit,
    onNavigateToVaccination: () -> Unit,
    onNavigateToHeatCycle: () -> Unit,
    onNavigateToAnalytics: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onViewCattle: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PremiumTopBar(
                title = "Gokula Health",
                subtitle = "Welcome back, Farmer"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Stats Row 1
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PremiumStatCard(
                            label = "Total My Cattle",
                            value = uiState.totalCows.toString(),
                            icon = Icons.Default.Face, // Using Face as cow placeholder
                            gradient = AgriGradients.PrimaryGradient,
                            modifier = Modifier.weight(1f)
                        )
                        PremiumStatCard(
                            label = "Milk Today",
                            value = "${uiState.todayMilkYield}L",
                            icon = Icons.Default.ShoppingCart, // Milk placeholder
                            gradient = AgriGradients.SecondaryGradient,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Row 2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        PremiumStatCard(
                            label = "Vaccinations",
                            value = uiState.upcomingVaccinations.toString(),
                            icon = Icons.Default.Info,
                            gradient = AgriGradients.AccentGradient,
                            modifier = Modifier.weight(1f)
                        )
                        PremiumStatCard(
                            label = "Heat Alerts",
                            value = uiState.upcomingHeatCycles.toString(),
                            icon = Icons.Default.Notifications,
                            gradient = AgriGradients.SecondaryGradient,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = "Management Tools",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    val menuItems = listOf(
                        HomeMenuItem("Add My Cattle", Icons.Default.Add, MaterialTheme.colorScheme.primary, onNavigateToAddCow),
                        HomeMenuItem("My Cattle", Icons.AutoMirrored.Filled.List, MaterialTheme.colorScheme.primary, onViewCattle),
                        HomeMenuItem("Milk Diary", Icons.Default.DateRange, Color(0xFF2196F3), onNavigateToMilkDiary),
                        HomeMenuItem("Schedules", Icons.Default.CheckCircle, Color(0xFFFF9800), onNavigateToVaccination),
                        HomeMenuItem("Heat Cycle", Icons.Default.Refresh, Color(0xFFE91E63), onNavigateToHeatCycle),
                        HomeMenuItem("Analytics", Icons.Default.Star, Color(0xFF9C27B0), onNavigateToAnalytics)
                    )

                    // Using a non-lazy grid inside scrollable column for better layout control
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        menuItems.chunked(2).forEach { rowItems ->
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                rowItems.forEach { item ->
                                    PremiumDashboardCard(
                                        title = item.title,
                                        icon = item.icon,
                                        color = item.color,
                                        onClick = item.onClick,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Simple Settings Button at bottom
                    OutlinedButton(
                        onClick = onNavigateToSettings,
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("App Settings")
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

data class HomeMenuItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val onClick: () -> Unit
)
