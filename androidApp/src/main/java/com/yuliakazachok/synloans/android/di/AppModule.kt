package com.yuliakazachok.synloans.android.di

import com.yuliakazachok.synloans.android.features.profile.presentation.ProfileViewModel
import com.yuliakazachok.synloans.android.features.requestcreate.presentation.RequestCreateViewModel
import com.yuliakazachok.synloans.android.features.requestdetail.presentation.RequestDetailViewModel
import com.yuliakazachok.synloans.android.features.requests.presentation.RequestsViewModel
import com.yuliakazachok.synloans.android.features.signin.presentation.SignInViewModel
import com.yuliakazachok.synloans.android.features.signup.presentation.SignUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

	viewModel { SignInViewModel() }
	viewModel { SignUpViewModel() }
	viewModel { ProfileViewModel() }
	viewModel { RequestsViewModel() }
	viewModel { RequestDetailViewModel() }
	viewModel { RequestCreateViewModel() }
}