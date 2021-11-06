package com.yuliakazachok.synloans.features.requestdetail.domain.entity

data class Borrower(
	val id: Int,
	val fullName: String,
	val shortName: String,
	val tin: String,
	val iec: String,
	val legalAddress: String,
	val actualAddress: String,
	val email: String,
)
