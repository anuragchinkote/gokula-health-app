package com.gokulahealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.local.entity.MilkEntryEntity
import com.gokulahealth.data.repository.CattleRepository
import com.gokulahealth.data.repository.MilkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MilkViewModel @Inject constructor(
    private val milkRepository: MilkRepository,
    private val cattleRepository: CattleRepository
) : ViewModel() {

    private val _selectedCowId = MutableStateFlow<Long?>(null)
    val selectedCowId: StateFlow<Long?> = _selectedCowId.asStateFlow()

    val cattleList: StateFlow<List<CowEntity>> = cattleRepository.getAllCows()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val milkHistory: StateFlow<List<MilkEntryEntity>> = _selectedCowId.flatMapLatest { id ->
        if (id == null) milkRepository.getAllMilkEntries()
        else milkRepository.getMilkEntriesForCow(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCow(cowId: Long?) {
        _selectedCowId.value = cowId
    }

    fun addMilkEntry(cowId: Long, morning: Double, evening: Double) {
        viewModelScope.launch {
            val total = morning + evening
            val entry = MilkEntryEntity(
                cowId = cowId,
                date = getStartOfDay(System.currentTimeMillis()),
                morningYield = morning,
                eveningYield = evening,
                totalYield = total
            )
            milkRepository.insertMilkEntry(entry)
        }
    }

    private fun getStartOfDay(time: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}
