package com.yuliakazachok.synloans.shared.user.domain.usecase

import com.yuliakazachok.synloans.shared.flag.domain.repository.FlagRepository
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository

class GetProfileUseCase(
    private val userRepository: UserRepository,
    private val flagRepository: FlagRepository,
) {

    suspend operator fun invoke(): Profile {
        val profile = userRepository.getProfile()
        flagRepository.set(profile.creditOrganisation)
        return profile
    }
}