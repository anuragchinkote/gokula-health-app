package com.gokulahealth.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.local.entity.CowEntity
import com.gokulahealth.data.repository.CattleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddCowViewModel @Inject constructor(
    private val repository: CattleRepository
) : ViewModel() {

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _earTagId = mutableStateOf("")
    val earTagId: State<String> = _earTagId

    private val _breed = mutableStateOf("")
    val breed: State<String> = _breed

    private val _age = mutableStateOf("")
    val age: State<String> = _age

    private val _weight = mutableStateOf("")
    val weight: State<String> = _weight

    private val _imageUri = mutableStateOf<String?>(null)
    val imageUri: State<String?> = _imageUri

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onNameChange(value: String) { _name.value = value }
    fun onEarTagChange(value: String) { _earTagId.value = value }
    fun onBreedChange(value: String) { _breed.value = value }
    fun onAgeChange(value: String) { _age.value = value }
    fun onWeightChange(value: String) { _weight.value = value }
    fun onImageChange(uri: String?) { _imageUri.value = uri }

    fun saveCow() {
        viewModelScope.launch {
            if (name.value.isBlank() || earTagId.value.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Name and Ear Tag are required"))
                return@launch
            }

            val cow = CowEntity(
                name = name.value,
                earTagId = earTagId.value,
                breed = breed.value,
                age = age.value.toIntOrNull() ?: 0,
                weight = weight.value.toDoubleOrNull() ?: 0.0,
                imageUri = imageUri.value,
                purchaseDate = System.currentTimeMillis(),
                dob = System.currentTimeMillis() // Simplification for now
            )

            repository.insertCow(cow)
            _eventFlow.emit(UiEvent.SaveSuccess)
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveSuccess : UiEvent()
    }
}
