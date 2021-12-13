package com.yuliakazachok.synloans.shared.bank.data.datasource

import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank

interface BankDataSource {

    suspend fun getDetail(id: Int, token: String): Bank
}