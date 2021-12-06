package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class CancelRequestUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(id: Int) {
        repository.cancel(id)
    }
}