package com.yuliakazachok.synloans.desktop.navigation

import com.yuliakazachok.synloans.desktop.core.AppTheme
import com.yuliakazachok.synloans.desktop.core.NavigationTree
import com.yuliakazachok.synloans.desktop.screens.SignInScreen
import com.yuliakazachok.synloans.desktop.screens.SignUpScreen
import org.koin.core.Koin
import ru.alexgladkov.odyssey.compose.extensions.screen
import ru.alexgladkov.odyssey.compose.navigation.RootComposeBuilder

fun buildComposeNavigationGraph(koin: Koin): RootComposeBuilder.() -> Unit {
    return { generateGraph(koin) }
}

fun RootComposeBuilder.generateGraph(koin: Koin) {

    screen(NavigationTree.Root.SignIn.name) {
        AppTheme {
            SignInScreen(rootController, koin)
        }
    }

    screen(NavigationTree.Root.SignUp.name) {
        AppTheme {
            SignUpScreen(rootController, koin)
        }
    }
}