package com.yuliakazachok.synloans.features.signin.domain.repository

import com.yuliakazachok.synloans.features.signin.domain.entity.Credentials

interface SignInRepository {

    suspend fun signIn(credentials: Credentials)
}