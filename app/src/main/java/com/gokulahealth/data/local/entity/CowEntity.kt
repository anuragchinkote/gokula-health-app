package com.gokulahealth.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cows")
data class CowEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val earTagId: String,
    val breed: String,
    val age: Int,
    val weight: Double,
    val imageUri: String?,
    val purchaseDate: Long,
    val dob: Long
)