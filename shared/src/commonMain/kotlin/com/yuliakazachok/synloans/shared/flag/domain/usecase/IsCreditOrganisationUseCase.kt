package com.yuliakazachok.synloans.shared.flag.domain.usecase

import com.yuliakazachok.synloans.shared.flag.domain.repository.FlagRepository

class IsCreditOrganisationUseCase(
    private val repository: FlagRepository,
) {

    operator fun invoke(): Boolean =
        repository.get()
}