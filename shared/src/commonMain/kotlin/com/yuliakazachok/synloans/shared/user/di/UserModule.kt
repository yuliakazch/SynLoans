package com.yuliakazachok.synloans.shared.user.di

import com.yuliakazachok.synloans.shared.user.data.datasource.UserDataSource
import com.yuliakazachok.synloans.shared.user.data.datasource.UserDataSourceImpl
import com.yuliakazachok.synloans.shared.user.data.repository.UserRepositoryImpl
import com.yuliakazachok.synloans.shared.user.domain.repository.UserRepository
import com.yuliakazachok.synloans.shared.user.domain.usecase.GetProfileUseCase
import com.yuliakazachok.synloans.shared.user.domain.usecase.UpdateProfileUseCase
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignInUseCase
import com.yuliakazachok.synloans.shared.user.domain.usecase.SignUpUseCase
import org.koin.dsl.module

val userModule = module {

    single<UserDataSource> { UserDataSourceImpl(get()) }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }

    single { SignInUseCase(get()) }
    single { SignUpUseCase(get()) }
    single { GetProfileUseCase(get(), get()) }
    single { UpdateProfileUseCase(get()) }
}