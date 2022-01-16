package com.yuliakazachok.synloans.android

import android.app.Application
import com.yuliakazachok.synloans.android.di.appModule
import com.yuliakazachok.synloans.di.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}