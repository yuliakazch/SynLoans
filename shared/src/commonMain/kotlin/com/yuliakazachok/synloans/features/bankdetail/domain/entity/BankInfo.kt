package com.yuliakazachok.synloans.features.bankdetail.domain.entity

data class BankInfo(
	val id: Int,
	val fullName: String,
	val shortName: String,
	val tin: String,
	val iec: String,
	val legalAddress: String,
	val actualAddress: String,
	val email: String,
)