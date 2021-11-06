package com.yuliakazachok.synloans.features.requestcreate.domain.entity

data class RequestCreateData(
	val sum: Int,
	val maxRate: Int,
	val term: Int,
)