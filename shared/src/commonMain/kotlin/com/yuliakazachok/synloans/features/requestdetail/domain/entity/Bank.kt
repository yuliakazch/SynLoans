package com.yuliakazachok.synloans.features.requestdetail.domain.entity

data class Bank(
	val id: Int,
	val name: String,
	val sum: Int,
	val creditOrganisation: Boolean,
)