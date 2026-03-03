package com.michambita.core.data.repository.impl

import com.michambita.core.data.database.dao.SynchronizationDAO
import com.michambita.core.data.database.entity.toDataBase
import com.michambita.core.data.database.entity.toDomain
import com.michambita.core.domain.model.Movimiento
import com.michambita.core.domain.repository.SynchronizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import java.util.Calendar

class SynchronizationRepositoryImpl @Inject constructor(
    private val synchronizationDAO: SynchronizationDAO
) : SynchronizationRepository {

    override fun getAllMovimientos(): Flow<List<Movimiento>> {
        return synchronizationDAO.findAll().map { list -> list.map { it.toDomain() } }
    }

    override fun getAllMovimientoPendientes(): Flow<List<Movimiento>> {
        return synchronizationDAO.findAllBySincronizado(false).map { list -> list.map { it.toDomain() } }
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
            val cutoffDate = calendar.timeInMillis
            synchronizationDAO.deleteOldSynchronizedMovimientos(cutoffDate)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
