package com.yuliakazachok.synloans.shared.user.domain.usecase

import com.yuliakazachok.synloans.shared.user.domain.entity.EditProfileInfo
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository


class UpdateProfileUseCase(
    private val repository: UserRepository,
) {

    suspend fun invoke(data: EditProfileInfo) {
        repository.updateProfile(data)
    }
}