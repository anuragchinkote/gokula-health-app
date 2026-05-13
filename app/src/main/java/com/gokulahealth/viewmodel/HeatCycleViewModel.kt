package com.gokulahealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.local.entity.HeatCycleEntity
import com.gokulahealth.data.repository.CattleRepository
import com.gokulahealth.data.repository.HeatCycleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HeatCycleViewModel @Inject constructor(
    private val heatCycleRepository: HeatCycleRepository,
    private val cattleRepository: CattleRepository
) : ViewModel() {

    val upcomingHeatCycles: StateFlow<List<HeatCycleEntity>> = heatCycleRepository.getUpcomingHeatCycles(System.currentTimeMillis())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cattleList: StateFlow<List<CowEntity>> = cattleRepository.getAllCows()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addHeatCycle(cowId: Long, cycleDate: Long, isPregnant: Boolean) {
        viewModelScope.launch {
            // Predict next cycle (average 21 days)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = cycleDate
            calendar.add(Calendar.DAY_OF_YEAR, 21)
            val nextExpectedDate = calendar.timeInMillis

            val heatCycle = HeatCycleEntity(
                cowId = cowId,
                cycleDate = cycleDate,
                nextExpectedDate = nextExpectedDate,
                isPregnant = isPregnant
            )
            heatCycleRepository.insertHeatCycle(heatCycle)
        }
    }
}
