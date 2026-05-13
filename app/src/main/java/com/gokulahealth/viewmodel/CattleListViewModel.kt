package com.gokulahealth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.repository.CattleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CattleListViewModel @Inject constructor(
    private val repository: CattleRepository
) : ViewModel() {

    val cattleList: StateFlow<List<CowEntity>> = repository.getAllCows()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
