package com.yuliakazachok.synloans.shared.user.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class EditProfileInfo(
    val shortName: String?,
    val legalAddress: String?,
    val actualAddress: String?,
)
