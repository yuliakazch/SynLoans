package com.yuliakazachok.synloans.shared.token.data.datasource

interface TokenDataSource {

    fun set(token: String)

    fun get(): String
}