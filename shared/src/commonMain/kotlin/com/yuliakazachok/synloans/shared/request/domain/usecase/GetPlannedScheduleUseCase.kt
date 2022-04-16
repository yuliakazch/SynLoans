package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class GetPlannedScheduleUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(id: Int): List<PaymentInfo> =
        repository.getPlannedSchedule(id)
}