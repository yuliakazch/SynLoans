package com.yuliakazachok.synloans.features.signin.di

import com.yuliakazachok.synloans.features.signin.data.SignInRepositoryImpl
import com.yuliakazachok.synloans.features.signin.domain.repository.SignInRepository
import org.koin.dsl.module

val signInModule = module {

    single<SignInRepository> { SignInRepositoryImpl() }
}