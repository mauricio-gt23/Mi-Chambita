package com.michambita.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.michambita.data.database.converter.Converters
import com.michambita.data.database.dao.SynchronizationDAO
import com.michambita.data.database.entity.MovimientoEntity

@Database(entities = [MovimientoEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class SynchronizationDB : RoomDatabase() {
    abstract fun synchronizationDAO(): SynchronizationDAO
}