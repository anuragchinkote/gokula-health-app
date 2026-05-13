package com.gokulahealth.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.*
import com.gokulahealth.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CowDetailsViewModel @Inject constructor(
    private val cattleRepository: CattleRepository,
    private val milkRepository: MilkRepository,
    private val vaccinationRepository: VaccinationRepository,
    private val heatCycleRepository: HeatCycleRepository,
    private val treatmentRepository: TreatmentRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cowId: Long = checkNotNull(savedStateHandle["cowId"])

    private val _cow = MutableStateFlow<CowEntity?>(null)
    val cow: StateFlow<CowEntity?> = _cow.asStateFlow()

    val milkEntries: StateFlow<List<MilkEntryEntity>> = milkRepository.getMilkEntriesForCow(cowId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vaccinations: StateFlow<List<VaccinationEntity>> = vaccinationRepository.getVaccinationsForCow(cowId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val heatCycles: StateFlow<List<HeatCycleEntity>> = heatCycleRepository.getHeatCyclesForCow(cowId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val treatments: StateFlow<List<TreatmentEntity>> = treatmentRepository.getTreatmentsForCow(cowId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadCow()
    }

    private fun loadCow() {
        viewModelScope.launch {
            _cow.value = cattleRepository.getCowById(cowId)
        }
    }

    fun deleteCow() {
        viewModelScope.launch {
            cow.value?.let {
                cattleRepository.deleteCow(it)
            }
        }
    }
}
