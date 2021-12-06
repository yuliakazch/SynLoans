package com.yuliakazachok.synloans.shared.bank.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Bank(
	val id: Int,
	val fullName: String,
	val shortName: String,
	val tin: String,
	val iec: String,
	val legalAddress: String,
	val actualAddress: String,
	val email: String,
)