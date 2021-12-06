package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class GetBankRequestsUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(): BankRequests =
        repository.getBankRequests()
}