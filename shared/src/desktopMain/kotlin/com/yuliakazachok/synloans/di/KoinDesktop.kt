package com.yuliakazachok.synloans.di

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.Settings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class)
actual val settingsModule = module {

    single<Settings> {
        Settings()
    }
}