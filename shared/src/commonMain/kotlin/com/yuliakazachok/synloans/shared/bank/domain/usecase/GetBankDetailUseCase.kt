package com.yuliakazachok.synloans.shared.bank.domain.usecase

import com.yuliakazachok.synloans.shared.bank.domain.entity.Bank
import com.yuliakazachok.synloans.shared.bank.domain.repository.BankRepository

class GetBankDetailUseCase(
    private val repository: BankRepository,
) {

    suspend operator fun invoke(id: Int): Bank =
        repository.getDetail(id)
}