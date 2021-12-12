package com.yuliakazachok.synloans.shared.flag.data.datasource

import com.russhwolf.settings.Settings

class FlagDataSourceImpl(
    private val settings: Settings,
) : FlagDataSource {

    private companion object {

        const val FLAG_KEY = "flag_key"
    }

    override fun set(flag: Boolean) {
        settings.putBoolean(FLAG_KEY, flag)
    }

    override fun get(): Boolean =
        settings.getBoolean(FLAG_KEY)
}