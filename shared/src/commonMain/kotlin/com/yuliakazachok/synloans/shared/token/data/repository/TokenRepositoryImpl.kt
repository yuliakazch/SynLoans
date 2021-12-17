package com.yuliakazachok.synloans.shared.token.data.repository

import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSource
import com.yuliakazachok.synloans.shared.token.domain.repository.TokenRepository

class TokenRepositoryImpl(
    private val dataSource: TokenDataSource,
) : TokenRepository {

    override fun isTokenExist(): Boolean =
        !dataSource.getOrNull().isNullOrEmpty()

    override fun clear() {
        dataSource.clear()
    }
}