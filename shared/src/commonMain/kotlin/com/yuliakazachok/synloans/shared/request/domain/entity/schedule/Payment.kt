package com.yuliakazachok.synloans.shared.request.domain.entity.schedule

import kotlinx.serialization.Serializable

@Serializable
data class Payment(
	val sum: Int,
	val date: String,
	val paid: Boolean,
)