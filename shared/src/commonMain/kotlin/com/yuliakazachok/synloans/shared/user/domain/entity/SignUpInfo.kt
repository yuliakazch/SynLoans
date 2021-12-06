package com.yuliakazachok.synloans.shared.user.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class SignUpInfo(
    val fullName: String = "",
    val shortName: String = "",
    val tin: String = "",
    val iec: String = "",
    val legalAddress: String = "",
    val actualAddress: String = "",
    val creditOrganisation: Boolean = false,
    val email: String = "",
    val password: String = "",
)