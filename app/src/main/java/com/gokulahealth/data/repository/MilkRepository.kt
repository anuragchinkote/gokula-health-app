package com.gokulahealth.data.repository

import com.gokulahealth.data.local.dao.MilkDao
import com.gokulahealth.data.local.entity.MilkEntryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MilkRepository @Inject constructor(
    private val milkDao: MilkDao
) {
    fun getMilkEntriesForCow(cowId: Long): Flow<List<MilkEntryEntity>> = 
        milkDao.getMilkEntriesForCow(cowId)

    fun getAllMilkEntries(): Flow<List<MilkEntryEntity>> = 
        milkDao.getAllMilkEntries()

    fun getMilkEntriesInRange(startDate: Long, endDate: Long): Flow<List<MilkEntryEntity>> =
        milkDao.getMilkEntriesInRange(startDate, endDate)

    suspend fun insertMilkEntry(entry: MilkEntryEntity) = 
        milkDao.insertMilkEntry(entry)

    suspend fun updateMilkEntry(entry: MilkEntryEntity) = 
        milkDao.updateMilkEntry(entry)

    suspend fun deleteMilkEntry(entry: MilkEntryEntity) = 
        milkDao.deleteMilkEntry(entry)

    fun getTotalYieldForDate(date: Long): Flow<Double?> = 
        milkDao.getTotalYieldForDate(date)
}