package com.yuliakazachok.synloans.shared.request.domain.usecase

import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment
import com.yuliakazachok.synloans.shared.request.domain.repository.RequestRepository

class GetActualScheduleUseCase(
    private val repository: RequestRepository,
) {

    suspend operator fun invoke(id: Int): List<Payment> =
        repository.getActualSchedule(id)
}