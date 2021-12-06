package com.yuliakazachok.synloans.shared.request.domain.entity.join

import kotlinx.serialization.Serializable

@Serializable
data class JoinSyndicateInfo(
    val sum: Int,
    val approveBankAgent: Boolean,
)