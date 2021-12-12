package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import com.yuliakazachok.synloans.shared.request.domain.entity.sum.Sum
import kotlinx.serialization.Serializable

@Serializable
data class RequestInfo(
	val id: Int,
	val status: StatusRequest,
	val sum: Sum,
	val maxRate: Float,
	val term: Int,
	val dateIssue: String?,
	val dateCreate: String,
)