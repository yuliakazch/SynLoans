package com.yuliakazachok.synloans.shared.request.domain.entity.create

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequestInfo(
	val sum: Int,
	val maxRate: Int,
	val term: Int,
)