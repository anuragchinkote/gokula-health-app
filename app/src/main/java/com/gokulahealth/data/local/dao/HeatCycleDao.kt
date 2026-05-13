package com.gokulahealth.data.local.dao

import androidx.room.*
import com.gokulahealth.data.local.entity.HeatCycleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeatCycleDao {
    @Query("SELECT * FROM heat_cycles WHERE cowId = :cowId ORDER BY cycleDate DESC")
    fun getHeatCyclesForCow(cowId: Long): Flow<List<HeatCycleEntity>>

    @Query("SELECT * FROM heat_cycles WHERE nextExpectedDate >= :currentTime ORDER BY nextExpectedDate ASC")
    fun getUpcomingHeatCycles(currentTime: Long): Flow<List<HeatCycleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeatCycle(heatCycle: HeatCycleEntity)

    @Update
    suspend fun updateHeatCycle(heatCycle: HeatCycleEntity)

    @Delete
    suspend fun deleteHeatCycle(heatCycle: HeatCycleEntity)
}