package com.yuliakazachok.synloans.features.requests.domain.entity

data class BorrowRequest(
    val id: Int,
    val dateIssue: String?,
    val dateCreate: String,
    val sum: Int,
)
