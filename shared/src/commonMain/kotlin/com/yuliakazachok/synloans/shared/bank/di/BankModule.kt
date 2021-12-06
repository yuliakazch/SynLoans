package com.yuliakazachok.synloans.shared.bank.di

import com.yuliakazachok.synloans.shared.bank.data.datasource.BankDataSource
import com.yuliakazachok.synloans.shared.bank.data.datasource.BankDataSourceImpl
import com.yuliakazachok.synloans.shared.bank.data.repository.BankRepositoryImpl
import com.yuliakazachok.synloans.shared.bank.domain.repository.BankRepository
import com.yuliakazachok.synloans.shared.bank.domain.usecase.GetBankDetailUseCase
import org.koin.dsl.module

val bankModule = module {

    single<BankDataSource> { BankDataSourceImpl(get()) }

    single<BankRepository> { BankRepositoryImpl(get()) }

    single { GetBankDetailUseCase(get()) }
}