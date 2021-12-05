package com.yuliakazachok.synloans.shared.user.signin.domain.repository

import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Credentials

interface SignInRepository {

    suspend fun signIn(credentials: Credentials)
}