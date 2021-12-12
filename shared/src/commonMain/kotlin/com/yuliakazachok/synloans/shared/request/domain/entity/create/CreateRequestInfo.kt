package com.yuliakazachok.synloans.shared.request.domain.entity.create

import com.yuliakazachok.synloans.shared.request.domain.entity.sum.Sum
import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestInfo(
	val sum: Sum,
	val maxRate: Int,
	val term: Int,
)