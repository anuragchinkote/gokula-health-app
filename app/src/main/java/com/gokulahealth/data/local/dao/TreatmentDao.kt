package com.gokulahealth.data.local.dao

import androidx.room.*
import com.gokulahealth.data.local.entity.TreatmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TreatmentDao {
    @Query("SELECT * FROM treatments WHERE cowId = :cowId ORDER BY date DESC")
    fun getTreatmentsForCow(cowId: Long): Flow<List<TreatmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreatment(treatment: TreatmentEntity)

    @Delete
    suspend fun deleteTreatment(treatment: TreatmentEntity)
}
