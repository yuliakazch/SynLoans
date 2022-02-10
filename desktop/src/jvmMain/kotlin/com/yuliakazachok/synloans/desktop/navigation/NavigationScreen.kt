package com.yuliakazachok.synloans.desktop.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class NavigationScreen : ScreenProvider {
    object SignIn : NavigationScreen()
    object SignUp : NavigationScreen()
    object Main : NavigationScreen()
}