package com.yuliakazachok.synloans.shared.bank.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class Bank(
    val id: Int,
    val fullName: String,
    val shortName: String,
    val inn: String,
    val kpp: String,
    val legalAddress: String,
    val actualAddress: String,
    val email: String = "email@mail.ru",
)