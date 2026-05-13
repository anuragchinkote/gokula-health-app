package com.gokulahealth.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.gokulahealth.ui.components.MilkAnalyticsCard
import com.gokulahealth.ui.components.PremiumTopBar
import com.gokulahealth.viewmodel.AnalyticsTimeRange
import com.gokulahealth.viewmodel.AnalyticsViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val uiState by viewModel.analyticsState.collectAsState()
    val timeRange by viewModel.timeRange.collectAsState()

    Scaffold(
        topBar = {
            PremiumTopBar(
                title = "Analytics",
                subtitle = "Milk dairy trends",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Premium Segmented Selector
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                AnalyticsTimeRange.entries.forEachIndexed { index, range ->
                    SegmentedButton(
                        selected = timeRange == range,
                        onClick = { viewModel.setTimeRange(range) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = AnalyticsTimeRange.entries.size)
                    ) {
                        Text(range.name.replace("_", " ").lowercase().replaceFirstChar { it.titlecase() })
                    }
                }
            }

            MilkAnalyticsCard(
                totalYield = "${String.format(Locale.getDefault(), "%.1f", uiState.totalYield)}L",
                avgYield = "${String.format(Locale.getDefault(), "%.1f", uiState.averageYield)}L",
                highestYield = "${String.format(Locale.getDefault(), "%.1f", uiState.highestProducerYield)}L"
            )

            // Dairy Trend Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Dairy Graph",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (uiState.dairyEntries.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            Text("No dairy data found", color = MaterialTheme.colorScheme.secondary)
                        }
                    } else {
                        MilkLineChart(uiState.dairyEntries.mapIndexed { index, entry ->
                            Entry(index.toFloat(), entry.totalYield.toFloat())
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun MilkLineChart(entries: List<Entry>) {
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val textColor = MaterialTheme.colorScheme.onSurface.toArgb()
    
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                setDrawGridBackground(false)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.textColor = textColor
                xAxis.setDrawGridLines(false)
                axisLeft.textColor = textColor
                axisRight.isEnabled = false
                legend.isEnabled = false
            }
        },
        update = { chart ->
            val dataSet = LineDataSet(entries, "Yield").apply {
                color = primaryColor
                setCircleColor(primaryColor)
                lineWidth = 3f
                circleRadius = 5f
                setDrawCircleHole(true)
                circleHoleColor = Color.White.toArgb()
                valueTextColor = textColor
                valueTextSize = 10f
                setDrawFilled(true)
                fillColor = primaryColor
                fillAlpha = 40
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            chart.data = LineData(dataSet)
            chart.animateY(1000)
            chart.invalidate()
        },
        modifier = Modifier.fillMaxSize()
    )
}
