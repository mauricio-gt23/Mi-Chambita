package com.michambita.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.michambita.data.database.entity.MovimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SynchronizationDAO {

    @Query("SELECT * FROM movimiento WHERE sincronizado = :estado")
    fun findAllBySincronizado(estado: Boolean): Flow<List<MovimientoEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(movimiento: MovimientoEntity)

    @Update
    suspend fun update(movimiento: MovimientoEntity)

    @Query("DELETE FROM movimiento WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM movimiento")
    suspend fun deleteAll()

    @Query("UPDATE movimiento SET sincronizado = 1 WHERE id IN (:ids)")
    suspend fun markAsSynchronized(ids: List<Long>)

    @Query("DELETE FROM movimiento WHERE sincronizado = 1 AND fecha_registro < :cutoffDate")
    suspend fun deleteOldSynchronizedMovimientos(cutoffDate: Date)
}