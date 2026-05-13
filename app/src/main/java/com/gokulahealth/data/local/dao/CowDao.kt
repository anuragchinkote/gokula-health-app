package com.gokulahealth.data.local.dao

import androidx.room.*
import com.gokulahealth.data.local.entity.CowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CowDao {
    @Query("SELECT * FROM cows ORDER BY id DESC")
    fun getAllCows(): Flow<List<CowEntity>>

    @Query("SELECT * FROM cows WHERE id = :id")
    suspend fun getCowById(id: Long): CowEntity?

    @Query("SELECT * FROM cows WHERE earTagId = :earTagId")
    suspend fun getCowByEarTag(earTagId: String): CowEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCow(cow: CowEntity): Long

    @Update
    suspend fun updateCow(cow: CowEntity)

    @Delete
    suspend fun deleteCow(cow: CowEntity)

    @Query("SELECT COUNT(*) FROM cows")
    fun getCowCount(): Flow<Int>
}