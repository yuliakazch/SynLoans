package com.yuliakazachok.synloans.shared.token.domain.usecase

import com.yuliakazachok.synloans.shared.token.domain.repository.TokenRepository

class IsTokenExistUseCase(
    private val repository: TokenRepository,
) {

    operator fun invoke(): Boolean =
        repository.isTokenExist()
}