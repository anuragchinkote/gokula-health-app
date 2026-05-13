package com.gokulahealth.data.repository

import com.gokulahealth.data.local.dao.TreatmentDao
import com.gokulahealth.data.local.entity.TreatmentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TreatmentRepository @Inject constructor(
    private val treatmentDao: TreatmentDao
) {
    fun getTreatmentsForCow(cowId: Long): Flow<List<TreatmentEntity>> =
        treatmentDao.getTreatmentsForCow(cowId)

    suspend fun insertTreatment(treatment: TreatmentEntity) =
        treatmentDao.insertTreatment(treatment)

    suspend fun deleteTreatment(treatment: TreatmentEntity) =
        treatmentDao.deleteTreatment(treatment)
}
