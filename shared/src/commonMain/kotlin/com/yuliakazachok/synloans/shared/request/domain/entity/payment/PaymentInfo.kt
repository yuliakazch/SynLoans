package com.yuliakazachok.synloans.shared.request.domain.entity.payment

import kotlinx.serialization.Serializable

@Serializable
data class PaymentInfo(
    val principal: Float,
    val percent: Float,
    val date: String,
)