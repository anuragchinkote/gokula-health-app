package com.gokulahealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.MilkEntryEntity
import com.gokulahealth.data.repository.MilkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val milkRepository: MilkRepository
) : ViewModel() {

    private val _timeRange = MutableStateFlow(AnalyticsTimeRange.LAST_7_DAYS)
    val timeRange: StateFlow<AnalyticsTimeRange> = _timeRange.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val milkData: StateFlow<List<MilkEntryEntity>> = _timeRange.flatMapLatest { range ->
        val endDate = System.currentTimeMillis()
        val startDate = getStartDateForRange(range)
        milkRepository.getMilkEntriesInRange(startDate, endDate)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val analyticsState = milkData.map { entries ->
        val totalYield = entries.sumOf { it.totalYield }
        val avgYield = if (entries.isNotEmpty()) totalYield / entries.size else 0.0
        val maxYieldEntry = entries.maxByOrNull { it.totalYield }
        
        AnalyticsUiState(
            totalYield = totalYield,
            averageYield = avgYield,
            highestProducerYield = maxYieldEntry?.totalYield ?: 0.0,
            dairyEntries = entries.sortedBy { it.date }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnalyticsUiState())

    fun setTimeRange(range: AnalyticsTimeRange) {
        _timeRange.value = range
    }

    private fun getStartDateForRange(range: AnalyticsTimeRange): Long {
        val calendar = Calendar.getInstance()
        when (range) {
            AnalyticsTimeRange.LAST_7_DAYS -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            AnalyticsTimeRange.LAST_30_DAYS -> calendar.add(Calendar.DAY_OF_YEAR, -30)
            AnalyticsTimeRange.ALL_TIME -> return 0L
        }
        return calendar.timeInMillis
    }
}

enum class AnalyticsTimeRange {
    LAST_7_DAYS, LAST_30_DAYS, ALL_TIME
}

data class AnalyticsUiState(
    val totalYield: Double = 0.0,
    val averageYield: Double = 0.0,
    val highestProducerYield: Double = 0.0,
    val dairyEntries: List<MilkEntryEntity> = emptyList()
)
