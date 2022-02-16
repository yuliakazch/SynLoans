package com.yuliakazachok.synloans.desktop.core

fun getIndexMonthText(count: Int): Int =
    when {
        count.rem(100) in 11..14 -> 2
        count.rem(10) == 1 -> 0
        count.rem(10) in 2..4 -> 1
        else -> 2
    }