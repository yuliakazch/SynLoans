package com.yuliakazachok.synloans.shared.token.di

import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSource
import com.yuliakazachok.synloans.shared.token.data.datasource.TokenDataSourceImpl
import org.koin.dsl.module

val tokenModule = module {

    single<TokenDataSource> { TokenDataSourceImpl(get()) }
}