package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import kotlinx.serialization.Serializable

@Serializable
enum class StatusRequest {
	OPEN,
	READY_TO_ISSUE,
	TRANSFER,
	ISSUE,
	CLOSE,
}