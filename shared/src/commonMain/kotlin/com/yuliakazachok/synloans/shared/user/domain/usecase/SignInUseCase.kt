package com.yuliakazachok.synloans.shared.user.domain.usecase

import com.yuliakazachok.synloans.shared.user.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository

class SignInUseCase(
    private val repository: UserRepository,
) {

    suspend operator fun invoke(credentials: Credentials) {
        repository.signIn(credentials)
    }
}