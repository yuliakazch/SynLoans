package com.yuliakazachok.synloans.shared.request.domain.entity.payment

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
    val payment: Long,
    val date: String? = null,
)