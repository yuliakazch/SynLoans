package com.yuliakazachok.synloans.features.requests.domain.entity

data class BankRequests(
	val own: List<BankRequest>,
	val other: List<BankRequest>,
)