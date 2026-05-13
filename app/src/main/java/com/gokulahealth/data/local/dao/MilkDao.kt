package com.gokulahealth.data.local.dao

import androidx.room.*
import com.gokulahealth.data.local.entity.MilkEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MilkDao {
    @Query("SELECT * FROM milk_entries WHERE cowId = :cowId ORDER BY date DESC")
    fun getMilkEntriesForCow(cowId: Long): Flow<List<MilkEntryEntity>>

    @Query("SELECT * FROM milk_entries ORDER BY date DESC")
    fun getAllMilkEntries(): Flow<List<MilkEntryEntity>>

    @Query("SELECT * FROM milk_entries WHERE date BETWEEN :startDate AND :endDate")
    fun getMilkEntriesInRange(startDate: Long, endDate: Long): Flow<List<MilkEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilkEntry(entry: MilkEntryEntity)

    @Update
    suspend fun updateMilkEntry(entry: MilkEntryEntity)

    @Delete
    suspend fun deleteMilkEntry(entry: MilkEntryEntity)

    @Query("SELECT SUM(totalYield) FROM milk_entries WHERE date = :date")
    fun getTotalYieldForDate(date: Long): Flow<Double?>
}