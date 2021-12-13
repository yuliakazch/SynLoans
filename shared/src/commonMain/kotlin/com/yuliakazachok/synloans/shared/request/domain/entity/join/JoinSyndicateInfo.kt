package com.yuliakazachok.synloans.shared.request.domain.entity.join

import com.yuliakazachok.synloans.shared.request.domain.entity.sum.Sum
import kotlinx.serialization.Serializable

@Serializable
data class JoinSyndicateInfo(
    val requestId: Int,
    val sum: Sum,
    val approveBankAgent: Boolean,
)