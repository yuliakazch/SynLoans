package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.list.BorrowRequest
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class GetBorrowRequestsUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(): List<BorrowRequest> =
        repository.getBorrowRequests()
}