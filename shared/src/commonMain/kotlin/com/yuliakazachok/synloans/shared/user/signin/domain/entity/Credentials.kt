package com.yuliakazachok.synloans.shared.user.signin.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Credentials(
    val email: String,
    val password: String,
)