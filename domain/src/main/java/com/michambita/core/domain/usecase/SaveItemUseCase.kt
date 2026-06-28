package com.michambita.domain.usecase

import com.michambita.domain.model.Item
import com.michambita.domain.repository.ItemRepository
import com.michambita.domain.repository.preference.CompanyPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SaveItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    private val companyPreferencesRepository: CompanyPreferencesRepository,
) {
    suspend operator fun invoke(item: Item): Result<Unit> {
        val company = companyPreferencesRepository.companyFlow.firstOrNull()
            ?: return Result.failure(Exception("No hay empresa en sesión"))
        item.companyId = company.id
        return itemRepository.saveItem(item)
    }
}
