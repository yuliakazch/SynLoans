package com.yuliakazachok.synloans.shared.flag.di

import com.yuliakazachok.synloans.shared.flag.data.datasource.FlagDataSource
import com.yuliakazachok.synloans.shared.flag.data.datasource.FlagDataSourceImpl
import com.yuliakazachok.synloans.shared.flag.data.repository.FlagRepositoryImpl
import com.yuliakazachok.synloans.shared.flag.domain.repository.FlagRepository
import com.yuliakazachok.synloans.shared.flag.domain.usecase.IsCreditOrganisationUseCase
import org.koin.dsl.module

val flagModule = module {

    single<FlagDataSource> { FlagDataSourceImpl(get()) }

    single<FlagRepository> { FlagRepositoryImpl(get()) }

    single { IsCreditOrganisationUseCase(get()) }
}