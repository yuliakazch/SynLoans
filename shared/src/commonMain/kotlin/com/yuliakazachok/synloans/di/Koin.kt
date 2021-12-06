package com.yuliakazachok.synloans.di

import com.yuliakazachok.synloans.shared.bank.di.bankModule
import com.yuliakazachok.synloans.shared.request.di.requestModule
import com.yuliakazachok.synloans.shared.user.di.userModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {

    startKoin {
        modules(networkModule)
        modules(userModule)
        modules(requestModule)
        modules(bankModule)
        appDeclaration()
    }
}

val networkModule = module {

    single {
        HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(createJson())
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }