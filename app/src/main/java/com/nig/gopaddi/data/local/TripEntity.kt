package com.nig.gopaddi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String,
    val title: String,
    val destination: String,
    val travelStyle: String,
    val description: String,
    val imageUrl: String?,
    val startDate: String,
    val endDate: String,
    val duration: String
)

