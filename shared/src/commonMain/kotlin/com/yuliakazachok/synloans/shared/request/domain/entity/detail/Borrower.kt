package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import kotlinx.serialization.Serializable

@Serializable
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