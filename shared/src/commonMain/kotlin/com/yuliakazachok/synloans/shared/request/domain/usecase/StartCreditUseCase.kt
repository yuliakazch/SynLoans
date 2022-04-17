package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class StartCreditUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(requestId: Int) {
        repository.startCredit(requestId)
    }
}