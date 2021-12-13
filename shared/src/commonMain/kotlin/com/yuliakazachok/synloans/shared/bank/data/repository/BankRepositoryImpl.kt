package com.yuliakazachok.synloans.shared.bank.data.repository

import com.yuliakazachok.synloans.shared.bank.data.datasource.BankDataSource
import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank
import com.yuliakazachok.synloans.shared.bank.domain.repository.BankRepository
import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSource

class BankRepositoryImpl(
    private val dataSource: BankDataSource,
    private val tokenDataSource: TokenDataSource,
) : BankRepository {

    override suspend fun getDetail(id: Int): Bank =
        dataSource.getDetail(id, tokenDataSource.get())
}