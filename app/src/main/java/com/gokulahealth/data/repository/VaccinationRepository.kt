package com.gokulahealth.data.repository

import com.gokulahealth.data.local.dao.VaccinationDao
import com.gokulahealth.data.local.entity.VaccinationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VaccinationRepository @Inject constructor(
    private val vaccinationDao: VaccinationDao
) {
    fun getVaccinationsForCow(cowId: Long): Flow<List<VaccinationEntity>> =
        vaccinationDao.getVaccinationsForCow(cowId)

    fun getAllUpcomingVaccinations(): Flow<List<VaccinationEntity>> =
        vaccinationDao.getAllUpcomingVaccinations()

    suspend fun insertVaccination(vaccination: VaccinationEntity) =
        vaccinationDao.insertVaccination(vaccination)

    suspend fun updateVaccination(vaccination: VaccinationEntity) =
        vaccinationDao.updateVaccination(vaccination)

    suspend fun deleteVaccination(vaccination: VaccinationEntity) =
        vaccinationDao.deleteVaccination(vaccination)
}