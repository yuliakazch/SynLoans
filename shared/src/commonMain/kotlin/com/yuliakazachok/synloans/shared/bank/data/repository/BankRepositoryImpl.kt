package com.yuliakazachok.synloans.shared.bank.data.repository

import com.yuliakazachok.synloans.shared.bank.data.datasource.BankDataSource
import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank
import com.yuliakazachok.synloans.shared.bank.domain.repository.BankRepository

class BankRepositoryImpl(
    private val dataSource: BankDataSource,
) : BankRepository {

    override suspend fun getDetail(id: Int): Bank =
        dataSource.getDetail(id)
}