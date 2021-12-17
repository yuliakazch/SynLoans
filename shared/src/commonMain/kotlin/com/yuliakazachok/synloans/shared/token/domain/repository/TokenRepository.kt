package com.yuliakazachok.synloans.shared.token.domain.repository

interface TokenRepository {

    fun isTokenExist(): Boolean

    fun clear()
}