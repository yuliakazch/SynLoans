package com.yuliakazachok.synloans.shared.user.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val fullName: String,
    val shortName: String,
    val inn: String,
    val kpp: String,
    val legalAddress: String,
    val actualAddress: String,
    val email: String,
    val creditOrganisation: Boolean,
)