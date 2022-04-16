package com.yuliakazachok.synloans.shared.request.domain.entity.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestInfo(
	val sum: Long,
	val maxRate: Float,
	val term: Int,
)