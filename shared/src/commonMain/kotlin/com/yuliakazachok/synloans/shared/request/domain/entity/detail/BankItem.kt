package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import kotlinx.serialization.Serializable

@Serializable
data class BankItem(
	val id: Int,
	val name: String,
	val sum: Int,
	val approveBankAgent: Boolean,
)