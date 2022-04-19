package com.yuliakazachok.synloans.android.features.paymentschedule.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleEffect
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleViewModel
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.ScheduleType
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PaymentScheduleDestination(navController: NavHostController, schedule: Int, requestId: Int) {

    val scheduleType = when (schedule) {
        0 -> ScheduleType.PLANNED
        1 -> ScheduleType.ACTUAL
        else -> throw NullPointerException("schedule is not correct")
    }
    val viewModel = getViewModel<PaymentScheduleViewModel> {
        parametersOf(scheduleType, requestId)
    }
    val state = viewModel.viewState.collectAsState().value

    PaymentScheduleScreen(
        state = state,
        effectFlow = viewModel.effect,
        onActionSent = { action -> viewModel.setEvent(action) },
        onNavigationRequested = { navigationEffect ->
            when (navigationEffect) {
                is PaymentScheduleEffect.Navigation.ToBack -> {
                    navController.popBackStack()
                }
            }
        }
    )
}