package com.yuliakazachok.synloans.shared.request.domain.entity.sum

import kotlinx.serialization.Serializable

@Serializable
enum class SumUnit {
    BILLION,
    MILLION,
    THOUSAND,
}