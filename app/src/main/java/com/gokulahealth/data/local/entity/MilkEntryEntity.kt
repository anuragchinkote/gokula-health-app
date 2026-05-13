package com.gokulahealth.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "milk_entries",
    foreignKeys = [
        ForeignKey(
            entity = CowEntity::class,
            parentColumns = ["id"],
            childColumns = ["cowId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["cowId"])]
)
data class MilkEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cowId: Long,
    val date: Long,
    val morningYield: Double,
    val eveningYield: Double,
    val totalYield: Double
)