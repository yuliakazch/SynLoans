package com.yuliakazachok.synloans.shared.bank.domain.repository

import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank

interface BankRepository {

    suspend fun getDetail(id: Int): Bank
}