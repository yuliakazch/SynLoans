package com.yuliakazachok.synloans.shared.flag.data.repository

import com.yuliakazachok.synloans.shared.flag.data.datasource.FlagDataSource
import com.yuliakazachok.synloans.shared.flag.domain.repository.FlagRepository

class FlagRepositoryImpl(
    private val dataSource: FlagDataSource,
) : FlagRepository {

    override fun set(flag: Boolean) {
        dataSource.set(flag)
    }

    override fun get(): Boolean =
        dataSource.get()
}