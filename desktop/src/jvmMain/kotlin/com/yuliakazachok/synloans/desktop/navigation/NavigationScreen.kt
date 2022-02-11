package com.yuliakazachok.synloans.desktop.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile

sealed class NavigationScreen : ScreenProvider {
    object SignIn : NavigationScreen()
    object SignUp : NavigationScreen()
    object Main : NavigationScreen()
    data class EditProfile(val profile: Profile) : NavigationScreen()
}