package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LoadAllItemsByCompanyIdUseCase @Inject constructor(
    private val companyPreferencesRepository: CompanyPreferencesRepository,
    private val itemRepository: ItemRepository
) {
    suspend fun invoke(): Result<List<Item>> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        val companyId = company.id
            ?: return Result.failure(Exception("El usuario no tiene empresa asociada"))
        return itemRepository.getAllItemsByCompanyId(companyId)
    }
}
