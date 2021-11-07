package com.yuliakazachok.synloans.features.editprofile.domain.entity

data class EditProfileData(
	val fullName: String,
	val shortName: String,
	val tin: String,
	val iec: String,
	val legalAddress: String,
	val actualAddress: String,
	val email: String,
)