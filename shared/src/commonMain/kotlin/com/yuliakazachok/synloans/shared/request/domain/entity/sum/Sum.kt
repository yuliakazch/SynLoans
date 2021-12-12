package com.yuliakazachok.synloans.shared.request.domain.entity.sum

import kotlinx.serialization.Serializable

@Serializable
data class Sum(
    val value: Int,
    val unit: SumUnit,
)