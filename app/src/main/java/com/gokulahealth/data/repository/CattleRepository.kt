package com.gokulahealth.data.repository

import com.gokulahealth.data.local.dao.CowDao
import com.gokulahealth.data.local.entity.CowEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CattleRepository @Inject constructor(
    private val cowDao: CowDao
) {
    fun getAllCows(): Flow<List<CowEntity>> = cowDao.getAllCows()

    suspend fun getCowById(id: Long): CowEntity? = cowDao.getCowById(id)

    suspend fun getCowByEarTag(earTagId: String): CowEntity? = cowDao.getCowByEarTag(earTagId)

    suspend fun insertCow(cow: CowEntity): Long = cowDao.insertCow(cow)

    suspend fun updateCow(cow: CowEntity) = cowDao.updateCow(cow)

    suspend fun deleteCow(cow: CowEntity) = cowDao.deleteCow(cow)

    fun getCowCount(): Flow<Int> = cowDao.getCowCount()
}