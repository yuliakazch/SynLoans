package com.yuliakazachok.synloans.desktop.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.ScheduleType
import com.yuliakazachok.synloans.shared.user.domain.entity.Profile

sealed class NavigationScreen : ScreenProvider {
    object SignIn : NavigationScreen()
    object SignUp : NavigationScreen()
    object ProfileInfo : NavigationScreen()
    object Requests : NavigationScreen()
    data class EditProfile(val profile: Profile) : NavigationScreen()
    data class RequestDetail(val requestId: Int, val participantBank: Boolean) : NavigationScreen()
    object RequestCreate : NavigationScreen()
    data class BankDetail(val bankId: Int) : NavigationScreen()
    data class PaymentSchedule(val requestId: Int, val scheduleType: ScheduleType) : NavigationScreen()
}