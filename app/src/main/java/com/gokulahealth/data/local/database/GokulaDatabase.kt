package com.gokulahealth.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gokulahealth.data.local.dao.*
import com.gokulahealth.data.local.entity.*

@Database(
    entities = [
        CowEntity::class,
        MilkEntryEntity::class,
        VaccinationEntity::class,
        HeatCycleEntity::class,
        TreatmentEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class GokulaDatabase : RoomDatabase() {
    abstract fun cowDao(): CowDao
    abstract fun milkDao(): MilkDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun heatCycleDao(): HeatCycleDao
    abstract fun treatmentDao(): TreatmentDao
}
