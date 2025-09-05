package com.michambita.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.michambita.data.database.entity.MovimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SynchronizationDAO {

    @Query("SELECT * FROM movimiento WHERE sincronizado = :estado")
    fun findAllBySincronizado(estado: Boolean): Flow<List<MovimientoEntity>>

    @Query("DELETE FROM movimiento WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM movimiento")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(synchronizationEntity: MovimientoEntity)
}