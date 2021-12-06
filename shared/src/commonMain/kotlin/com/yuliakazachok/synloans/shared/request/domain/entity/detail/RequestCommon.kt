package com.yuliakazachok.synloans.shared.request.domain.entity.detail

import kotlinx.serialization.Serializable

@Serializable
data class RequestCommon(
    val info: RequestInfo,
    val banks: List<BankItem>,
    val borrower: Borrower,
)