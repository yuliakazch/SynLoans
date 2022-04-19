package com.yuliakazachok.synloans.shared.request.domain.entity.list

import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import kotlinx.serialization.Serializable

@Serializable
data class BankRequests(
	val own: List<RequestCommon>,
	val other: List<RequestCommon>,
)