package com.yuliakazachok.synloans.shared.user.signin.di

import com.yuliakazachok.synloans.shared.user.signin.data.datasource.SignInDataSource
import com.yuliakazachok.synloans.shared.user.signin.data.datasource.SignInDataSourceImpl
import com.yuliakazachok.synloans.shared.user.signin.data.repository.SignInRepositoryImpl
import com.yuliakazachok.synloans.shared.user.signin.domain.repository.SignInRepository
import com.yuliakazachok.synloans.shared.user.signin.domain.usecase.SignInUseCase
import org.koin.dsl.module

val signInModule = module {

    single<SignInDataSource> { SignInDataSourceImpl(get()) }

    single<SignInRepository> { SignInRepositoryImpl(get()) }

    single { SignInUseCase(get()) }
}