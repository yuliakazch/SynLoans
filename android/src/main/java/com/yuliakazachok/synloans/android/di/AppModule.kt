package com.yuliakazachok.synloans.android.di

import com.yuliakazachok.synloans.android.features.bankdetail.presentation.BankDetailViewModel
import com.yuliakazachok.synloans.android.features.editprofile.presentation.EditProfileViewModel
import com.yuliakazachok.synloans.android.features.joinsyndicate.presentation.JoinSyndicateViewModel
import com.yuliakazachok.synloans.android.features.makepayment.presentation.MakePaymentViewModel
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.PaymentScheduleViewModel
import com.yuliakazachok.synloans.android.features.paymentschedule.presentation.ScheduleType
import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileViewModel
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateViewModel
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailViewModel
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsViewModel
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInViewModel
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpViewModel
import com.yuliakazachok.synloans.android.features.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { EditProfileViewModel(get(), get()) }
    viewModel { RequestsViewModel(get(), get(), get()) }
    viewModel { (requestId: Int) ->
        RequestDetailViewModel(get(), get(), get(), get(), get(), requestId)
    }
    viewModel { RequestCreateViewModel(get()) }
    viewModel { (requestId: Int) ->
        JoinSyndicateViewModel(get(), requestId)
    }
    viewModel { (bankId: Int) ->
        BankDetailViewModel(get(), bankId)
    }
    viewModel { (scheduleType: ScheduleType, requestId: Int) ->
        PaymentScheduleViewModel(get(), get(), scheduleType, requestId)
    }
    viewModel { SplashViewModel(get()) }
    viewModel { (requestId: Int) ->
        MakePaymentViewModel(get(), requestId)
    }
}