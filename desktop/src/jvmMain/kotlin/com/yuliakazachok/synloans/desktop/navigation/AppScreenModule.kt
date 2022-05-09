package com.yuliakazachok.synloans.desktop.navigation

import cafe.adriel.voyager.core.registry.screenModule
import com.yuliakazachok.synloans.desktop.screens.*

val appScreenModule = screenModule {
    register<NavigationScreen.SignIn> { SignInScreen() }
    register<NavigationScreen.SignUp> { SignUpScreen() }
    register<NavigationScreen.ProfileInfo> { ProfileScreen() }
    register<NavigationScreen.Requests> { RequestsScreen() }
    register<NavigationScreen.EditProfile> { EditProfileScreen(it.profile) }
    register<NavigationScreen.RequestDetail> { RequestDetailScreen(it.requestId, it.participantBank) }
    register<NavigationScreen.RequestCreate> { RequestCreateScreen() }
    register<NavigationScreen.BankDetail> { BankDetailScreen(it.bankId) }
    register<NavigationScreen.PaymentSchedule> { PaymentScheduleScreen(it.requestId, it.scheduleType) }
}