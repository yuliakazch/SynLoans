package com.yuliakazachok.synloans.shared.user.signin.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val value: String,
)