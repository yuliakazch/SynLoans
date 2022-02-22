package com.yuliakazachok.synloans.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.navigator.Navigator
import com.yuliakazachok.synloans.desktop.core.AppTheme
import com.yuliakazachok.synloans.desktop.navigation.appScreenModule
import com.yuliakazachok.synloans.desktop.screens.SignInScreen
import com.yuliakazachok.synloans.di.initKoin

val koin = initKoin().koin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Syndicated Loans",
        state = rememberWindowState(width = 900.dp, height = 700.dp)
    ) {
        AppTheme {
            ScreenRegistry {
                appScreenModule()
            }
            Navigator(SignInScreen())
        }
    }
}