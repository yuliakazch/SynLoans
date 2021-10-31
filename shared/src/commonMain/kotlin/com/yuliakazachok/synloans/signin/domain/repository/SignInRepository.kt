package com.yuliakazachok.synloans.signin.domain.repository

import com.yuliakazachok.synloans.signin.domain.entity.Credentials

interface SignInRepository {

    suspend fun signIn(credentials: Credentials)
}