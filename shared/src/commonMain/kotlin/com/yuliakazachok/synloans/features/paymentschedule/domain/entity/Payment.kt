package com.yuliakazachok.synloans.features.paymentschedule.domain.entity

data class Payment(
	val sum: Int,
	val date: String,
	val paid: Boolean,
)
