package com.yuliakazachok.synloans.shared.token.data.datasource

import com.russhwolf.settings.Settings

class TokenDataSourceImpl(
    private val settings: Settings,
) : TokenDataSource {

    private companion object {

        const val TOKEN_KEY = "token_key"
    }

    override fun set(token: String) {
        settings.putString(TOKEN_KEY, token)
    }

    override fun get(): String =
        settings.getString(TOKEN_KEY)
}