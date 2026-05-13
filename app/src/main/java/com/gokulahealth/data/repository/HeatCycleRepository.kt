package com.gokulahealth.data.repository

import com.gokulahealth.data.local.dao.HeatCycleDao
import com.gokulahealth.data.local.entity.HeatCycleEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HeatCycleRepository @Inject constructor(
    private val heatCycleDao: HeatCycleDao
) {
    fun getHeatCyclesForCow(cowId: Long): Flow<List<HeatCycleEntity>> =
        heatCycleDao.getHeatCyclesForCow(cowId)

    fun getUpcomingHeatCycles(currentTime: Long): Flow<List<HeatCycleEntity>> =
        heatCycleDao.getUpcomingHeatCycles(currentTime)

    suspend fun insertHeatCycle(heatCycle: HeatCycleEntity) =
        heatCycleDao.insertHeatCycle(heatCycle)

    suspend fun updateHeatCycle(heatCycle: HeatCycleEntity) =
        heatCycleDao.updateHeatCycle(heatCycle)

    suspend fun deleteHeatCycle(heatCycle: HeatCycleEntity) =
        heatCycleDao.deleteHeatCycle(heatCycle)
}