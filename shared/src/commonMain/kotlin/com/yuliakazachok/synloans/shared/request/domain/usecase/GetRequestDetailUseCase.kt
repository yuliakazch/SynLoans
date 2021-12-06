package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class GetRequestDetailUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(id: Int): RequestCommon =
        repository.getRequestDetail(id)
}