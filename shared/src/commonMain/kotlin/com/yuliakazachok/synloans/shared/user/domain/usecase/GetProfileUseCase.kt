package com.yuliakazachok.synloans.shared.user.domain.usecase

import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository

class GetProfileUseCase(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(): Profile =
        repository.getProfile()
}