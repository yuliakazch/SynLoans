package com.yuliakazachok.synloans.features.requestdetail.domain.entity

data class RequestInfo(
	val id: Int,
	val sum: Int,
	val maxRate: Int,
	val term: Int,
	val dateIssue: String?,
	val dateCreate: String,
)
