package com.michambita.data.repository.impl

import com.michambita.data.database.dao.SynchronizationDAO
import com.michambita.data.database.entity.toDataBase
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import java.util.Calendar

class SynchronizationRepositoryImpl @Inject constructor(
    private val synchronizationDAO: SynchronizationDAO
) : SynchronizationRepository {

    override fun getAllMovimientoPendientes(): Flow<List<Movimiento>> {
        return synchronizationDAO.findAllBySincronizado(false).map{list -> list.map { it.toDomain() } }
    }

    override suspend fun addMovimiento(movimiento: Movimiento): Result<Unit> {
        return try {
            synchronizationDAO.insert(movimiento.toDataBase())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMovimiento(movimiento: Movimiento): Result<Unit> {
        return try {
            synchronizationDAO.update(movimiento.toDataBase())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMovimiento(movimiento: Movimiento): Result<Unit> {
        return try {
            synchronizationDAO.deleteById(movimiento.id!!)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAllMovimientoPendientes(): Result<Unit> {
        return try {
            synchronizationDAO.deleteAll()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markMultipleAsSynchronized(ids: List<Long>): Result<Unit> {
        return try {
            synchronizationDAO.markAsSynchronized(ids)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cleanOldSynchronizedMovimientos(daysOld: Int): Result<Unit> {
        return try {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -daysOld)
            val cutoffDate = calendar.time
            synchronizationDAO.deleteOldSynchronizedMovimientos(cutoffDate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}