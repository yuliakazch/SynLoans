package com.yuliakazachok.synloans.android.features.signup.presentation

data class SignUpData(
    val fullName: String = "",
    val shortName: String = "",
    val tin: String = "",
    val iec: String = "",
    val legalAddress: String = "",
    val actualAddress: String = "",
    val creditOrganisation: Boolean = false,
    val email: String = "",
    val password: String = "",
    val passwordAgain: String = "",
)