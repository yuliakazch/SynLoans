package com.yuliakazachok.synloans.shared.user.signin.data.datasource

import com.yuliakazachok.synloans.shared.user.signin.domain.entity.Credentials

interface SignInDataSource {

    suspend fun signIn(credentials: Credentials)
}