package com.yuliakazachok.synloans.desktop.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.yuliakazachok.synloans.desktop.screens.MainScreen
import com.yuliakazachok.synloans.desktop.screens.SignInScreen
import com.yuliakazachok.synloans.desktop.screens.SignUpScreen

val appScreenModule = screenModule {
    register<NavigationScreen.SignIn> { SignInScreen() }
    register<NavigationScreen.SignUp> { SignUpScreen() }
    register<NavigationScreen.Main> { MainScreen() }
}