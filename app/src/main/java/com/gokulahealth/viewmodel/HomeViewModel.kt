package com.gokulahealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.repository.CattleRepository
import com.gokulahealth.data.repository.HeatCycleRepository
import com.gokulahealth.data.repository.MilkRepository
import com.gokulahealth.data.repository.VaccinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    cattleRepository: CattleRepository,
    milkRepository: MilkRepository,
    vaccinationRepository: VaccinationRepository,
    heatCycleRepository: HeatCycleRepository
) : ViewModel() {

    // Get the start of today in milliseconds
    private val todayStart: Long
        get() = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    val totalCows = cattleRepository.getCowCount()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayMilkYield = milkRepository.getTotalYieldForDate(todayStart)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val upcomingVaccinationsCount = vaccinationRepository.getAllUpcomingVaccinations()
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val upcomingHeatCyclesCount = heatCycleRepository.getUpcomingHeatCycles(System.currentTimeMillis())
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // UI State for the Dashboard
    val uiState = combine(
        totalCows,
        todayMilkYield,
        upcomingVaccinationsCount,
        upcomingHeatCyclesCount
    ) { total, yield, vaccinations, heatCycles ->
        HomeUiState(
            totalCows = total,
            todayMilkYield = yield ?: 0.0,
            upcomingVaccinations = vaccinations,
            upcomingHeatCycles = heatCycles
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}

data class HomeUiState(
    val totalCows: Int = 0,
    val todayMilkYield: Double = 0.0,
    val upcomingVaccinations: Int = 0,
    val upcomingHeatCycles: Int = 0
)