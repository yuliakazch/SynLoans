package com.yuliakazachok.synloans.shared.flag.domain.repository

interface FlagRepository {

    fun set(flag: Boolean)

    fun get(): Boolean
}