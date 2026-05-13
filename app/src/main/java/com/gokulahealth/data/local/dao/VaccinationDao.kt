package com.gokulahealth.data.local.dao

import androidx.room.*
import com.gokulahealth.data.local.entity.VaccinationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccinations WHERE cowId = :cowId ORDER BY vaccinationDate DESC")
    fun getVaccinationsForCow(cowId: Long): Flow<List<VaccinationEntity>>

    @Query("SELECT * FROM vaccinations ORDER BY nextDueDate ASC")
    fun getAllUpcomingVaccinations(): Flow<List<VaccinationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccination(vaccination: VaccinationEntity)

    @Update
    suspend fun updateVaccination(vaccination: VaccinationEntity)

    @Delete
    suspend fun deleteVaccination(vaccination: VaccinationEntity)
}