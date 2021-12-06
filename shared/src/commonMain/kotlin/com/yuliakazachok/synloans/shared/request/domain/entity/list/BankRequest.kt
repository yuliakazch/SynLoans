package com.yuliakazachok.synloans.shared.request.domain.entity.list

import kotlinx.serialization.Serializable

@Serializable
data class BankRequest(
	val id: Int,
	val name: String,
	val sum: Int,
)