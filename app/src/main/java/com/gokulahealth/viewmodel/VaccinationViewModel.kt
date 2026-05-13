package com.gokulahealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.local.entity.VaccinationEntity
import com.gokulahealth.data.repository.CattleRepository
import com.gokulahealth.data.repository.VaccinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class VaccinationViewModel @Inject constructor(
    private val vaccinationRepository: VaccinationRepository,
    private val cattleRepository: CattleRepository
) : ViewModel() {

    val upcomingVaccinations: StateFlow<List<VaccinationEntity>> = vaccinationRepository.getAllUpcomingVaccinations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cattleList: StateFlow<List<CowEntity>> = cattleRepository.getAllCows()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addVaccination(cowId: Long, name: String, date: Long, nextDueDate: Long) {
        viewModelScope.launch {
            val vaccination = VaccinationEntity(
                cowId = cowId,
                vaccineName = name,
                vaccinationDate = date,
                nextDueDate = nextDueDate
            )
            vaccinationRepository.insertVaccination(vaccination)
            
            // In a real app, we would schedule the notification here using AlarmManager
        }
    }
}
