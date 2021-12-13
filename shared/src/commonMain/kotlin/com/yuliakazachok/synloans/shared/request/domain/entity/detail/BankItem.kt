package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import com.yuliakazachok.synloans.shared.request.domain.entity.sum.Sum
import kotlinx.serialization.Serializable

@Serializable
data class BankItem(
	val id: Int,
	val name: String,
	val sum: Sum,
	val approveBankAgent: Boolean,
)