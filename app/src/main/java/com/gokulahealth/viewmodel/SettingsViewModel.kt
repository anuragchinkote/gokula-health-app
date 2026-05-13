package com.gokulahealth.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gokulahealth.data.repository.CattleRepository
import com.gokulahealth.data.repository.MilkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val cattleRepository: CattleRepository,
    private val milkRepository: MilkRepository
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun exportDataToCsv() {
        viewModelScope.launch {
            try {
                val cows = cattleRepository.getAllCows().first()
                val milkEntries = milkRepository.getAllMilkEntries().first()

                val csvFile = File(application.cacheDir, "gokula_health_export.csv")
                csvFile.bufferedWriter().use { writer ->
                    // Cows Header
                    writer.write("--- MY CATTLE ---\n")
                    writer.write("ID,Name,Ear Tag ID,Breed,Age,Weight\n")
                    cows.forEach { cow ->
                        writer.write("${cow.id},${cow.name},${cow.earTagId},${cow.breed},${cow.age},${cow.weight}\n")
                    }
                    
                    writer.write("\n--- MILK DIARY ENTRIES ---\n")
                    writer.write("Cow ID,Date,Morning Yield,Evening Yield,Total Yield\n")
                    milkEntries.forEach { entry ->
                        writer.write("${entry.cowId},${entry.date},${entry.morningYield},${entry.eveningYield},${entry.totalYield}\n")
                    }
                }

                _eventFlow.emit(UiEvent.ExportSuccess(csvFile))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Export failed: ${e.message}"))
            }
        }
    }

    fun shareCsvFile(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export My Cattle Data"))
    }

    sealed class UiEvent {
        data class ExportSuccess(val file: File) : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
