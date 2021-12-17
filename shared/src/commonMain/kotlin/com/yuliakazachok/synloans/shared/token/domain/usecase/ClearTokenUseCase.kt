package com.yuliakazachok.synloans.shared.token.domain.usecase

import com.yuliakazachok.synloans.shared.token.domain.repository.TokenRepository

class ClearTokenUseCase(
    private val repository: TokenRepository,
) {

    operator fun invoke() {
        repository.clear()
    }
}