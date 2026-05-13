package com.gokulahealth.di

import android.content.Context
import androidx.room.Room
import com.gokulahealth.data.local.dao.*
import com.gokulahealth.data.local.database.GokulaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): GokulaDatabase {
        return Room.databaseBuilder(
            context,
            GokulaDatabase::class.java,
            "gokula_health_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideCowDao(db: GokulaDatabase): CowDao = db.cowDao()

    @Provides
    fun provideMilkDao(db: GokulaDatabase): MilkDao = db.milkDao()

    @Provides
    fun provideVaccinationDao(db: GokulaDatabase): VaccinationDao = db.vaccinationDao()

    @Provides
    fun provideHeatCycleDao(db: GokulaDatabase): HeatCycleDao = db.heatCycleDao()

    @Provides
    fun provideTreatmentDao(db: GokulaDatabase): TreatmentDao = db.treatmentDao()
}