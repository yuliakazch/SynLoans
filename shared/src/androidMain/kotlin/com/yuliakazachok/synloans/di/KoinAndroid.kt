package com.yuliakazachok.synloans.di

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

actual val settingsModule = module {

    single<Settings> {
        AndroidSettings(getSharedPreferences(get()))
    }
}

private fun getSharedPreferences(context: Context) = context.getSharedPreferences("references", Context.MODE_PRIVATE)