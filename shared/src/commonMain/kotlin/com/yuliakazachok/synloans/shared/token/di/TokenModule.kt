package com.yuliakazachok.synloans.shared.token.di

import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSource
import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSourceImpl
import com.yuliakazachok.synloans.shared.token.data.repository.TokenRepositoryImpl
import com.yuliakazachok.synloans.shared.token.domain.repository.TokenRepository
import com.yuliakazachok.synloans.shared.token.domain.usecase.ClearTokenUseCase
import com.yuliakazachok.synloans.shared.token.domain.usecase.IsTokenExistUseCase
import org.koin.dsl.module

val tokenModule = module {

    single<TokenDataSource> { TokenDataSourceImpl(get()) }

    single<TokenRepository> { TokenRepositoryImpl(get()) }

    single { IsTokenExistUseCase(get()) }
    single { ClearTokenUseCase(get()) }
}