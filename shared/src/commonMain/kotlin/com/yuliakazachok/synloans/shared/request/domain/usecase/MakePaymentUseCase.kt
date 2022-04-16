package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class MakePaymentUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(requestId: Int, data: Payment) {
        repository.makePayment(requestId, data)
    }
}