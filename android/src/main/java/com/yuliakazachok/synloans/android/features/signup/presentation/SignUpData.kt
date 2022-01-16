package com.yuliakazachok.synloans.android.features.signup.presentation

data class SignUpData(
    val fullName: String = "",
    val shortName: String = "",
    val inn: String = "",
    val kpp: String = "",
    val legalAddress: String = "",
    val actualAddress: String = "",
    val creditOrganisation: Boolean = false,
    val email: String = "",
    val password: String = "",
    val passwordAgain: String = "",
)