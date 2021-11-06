package com.yuliakazachok.synloans.features.requestdetail.domain.entity

data class RequestCommon(
	val info: RequestInfo,
	val banks: List<Bank>,
	val borrower: Borrower,
)
