package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import kotlinx.serialization.Serializable

@Serializable
data class Borrower(
	val id: Int,
	val fullName: String,
	val shortName: String,
	val inn: String,
	val kpp: String,
	val legalAddress: String,
	val actualAddress: String,
	val email: String = "email@mail.ru",
)