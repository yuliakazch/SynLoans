package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class CreateRequestUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(data: CreateRequestInfo) {
        repository.create(data)
    }
}