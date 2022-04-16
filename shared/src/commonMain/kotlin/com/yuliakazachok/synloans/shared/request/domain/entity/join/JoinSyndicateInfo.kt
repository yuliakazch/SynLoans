package com.yuliakazachok.synloans.shared.request.domain.entity.join

import kotlinx.serialization.Serializable

@Serializable
data class JoinSyndicateInfo(
    val requestId: Int,
    val sum: Long,
    val approveBankAgent: Boolean,
)