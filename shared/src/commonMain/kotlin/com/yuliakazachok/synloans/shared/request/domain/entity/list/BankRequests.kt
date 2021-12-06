package com.yuliakazachok.synloans.shared.request.domain.entity.list

data class BankRequests(
	val own: List<BankRequest>,
	val other: List<BankRequest>,
)