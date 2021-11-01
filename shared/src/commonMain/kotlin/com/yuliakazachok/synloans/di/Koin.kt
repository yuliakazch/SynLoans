package com.yuliakazachok.synloans.di

import com.yuliakazachok.synloans.signin.di.signInModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {

    startKoin {
        modules(coreModule)
        appDeclaration()
    }
}

private val coreModule = module {
    signInModule
}