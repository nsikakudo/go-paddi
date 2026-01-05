package com.nig.gopaddi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TripEntity::class], version = 1)
abstract class TripDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}