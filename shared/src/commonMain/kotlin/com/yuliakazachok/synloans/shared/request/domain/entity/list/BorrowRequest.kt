package com.yuliakazachok.synloans.shared.request.domain.entity.list

import kotlinx.serialization.Serializable

@Serializable
data class BorrowRequest(
    val id: Int,
    val dateIssue: String?,
    val dateCreate: String,
    val sum: Int,
)