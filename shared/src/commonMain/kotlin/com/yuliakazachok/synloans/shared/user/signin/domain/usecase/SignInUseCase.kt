package com.yuliakazachok.synloans.shared.user.signin.domain.usecase

import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Credentials
import com.yuliakazachok.synloans.shared.user.signin.domain.repository.SignInRepository

class SignInUseCase(
    private val repository: SignInRepository
) {

    suspend operator fun invoke(credentials: Credentials) {
        repository.signIn(credentials)
    }
}