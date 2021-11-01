package com.yuliakazachok.synloans.android.di

import com.yuliakazachok.synloans.android.features.signin.presentation.SignInViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { SignInViewModel() }
}