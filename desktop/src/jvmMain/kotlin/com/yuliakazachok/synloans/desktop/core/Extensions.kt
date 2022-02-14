package com.yuliakazachok.synloans.desktop.core

import com.yuliakazachok.synloans.shared.request.domain.entity.sum.SumUnit

fun SumUnit.getTextResource() =
    when (this) {
        SumUnit.BILLION -> TextResources.billionSum
        SumUnit.MILLION -> TextResources.millionSum
        SumUnit.THOUSAND -> TextResources.thousandSum
    }