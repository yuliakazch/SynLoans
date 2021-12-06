package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import kotlinx.serialization.Serializable

@Serializable
data class RequestInfo(
	val id: Int,
	val status: StatusRequest,
	val sum: Int,
	val maxRate: Int,
	val term: Int,
	val dateIssue: String?,
	val dateCreate: String,
)