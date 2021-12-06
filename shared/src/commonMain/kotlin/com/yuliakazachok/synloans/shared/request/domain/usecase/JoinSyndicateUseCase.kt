package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class JoinSyndicateUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(data: JoinSyndicateInfo) {
        repository.join(data)
    }
}