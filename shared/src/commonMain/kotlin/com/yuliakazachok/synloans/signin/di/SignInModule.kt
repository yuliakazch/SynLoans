package com.yuliakazachok.synloans.signin.di

import com.yuliakazachok.synloans.signin.data.SignInRepositoryImpl
import com.yuliakazachok.synloans.signin.domain.repository.SignInRepository
import org.koin.dsl.module

val signInModule = module {

    single<SignInRepository> { SignInRepositoryImpl() }
}