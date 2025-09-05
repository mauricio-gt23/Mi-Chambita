package com.michambita.data.repository.impl

import com.michambita.data.database.dao.SynchronizationDAO
import com.michambita.data.repository.SynchronizationRepository
import com.michambita.domain.model.Movimiento
import com.michambita.domain.model.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SynchronizationRepositoryImpl @Inject constructor(
    private val synchronizationDAO: SynchronizationDAO
) : SynchronizationRepository {

    override fun getAllMovimientoPendientes(): Flow<List<Movimiento>> {
        return synchronizationDAO.findAllBySincronizado(false).map{list -> list.map { it.toDomain() } }
    }

}