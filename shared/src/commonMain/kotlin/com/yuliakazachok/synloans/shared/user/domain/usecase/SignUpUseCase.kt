package com.yuliakazachok.synloans.shared.user.domain.usecase

import com.yuliakazachok.synloans.shared.user.domain.entity.SignUpInfo
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository

class SignUpUseCase(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(data: SignUpInfo) {
        repository.signUp(data)
    }
}