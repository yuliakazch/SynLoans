package com.yuliakazachok.synloans.shared.flag.data.datasource

interface FlagDataSource {

    fun set(flag: Boolean)

    fun get(): Boolean
}