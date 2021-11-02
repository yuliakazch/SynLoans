package com.yuliakazachok.synloans.features.profile.domain.entity

data class Profile(
    val fullName: String,
    val shortName: String,
    val tin: String,
    val iec: String,
    val legalAddress: String,
    val actualAddress: String,
    val email: String,
    val creditOrganisation: Boolean,
)
