package com.yuliakazachok.synloans.shared.user.data.datasource

import com.yuliakazachok.synloans.shared.user.domain.entity.*
import com.yuliakazachok.synloans.util.Config.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserDataSourceImpl(
    private val httpClient: HttpClient,
) : UserDataSource {

    override suspend fun signIn(credentials: Credentials): Token =
        httpClient.post<Token>("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            body = credentials
        }

    override suspend fun signUp(data: SignUpInfo) {
        httpClient.post<Unit>("$BASE_URL/registration") {
            contentType(ContentType.Application.Json)
            body = data
        }
    }

    override suspend fun getProfile(token: String): Profile =
        httpClient.get<Profile>("$BASE_URL/profile/") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun updateProfile(data: EditProfileInfo, token: String) {
        httpClient.put<Unit>("$BASE_URL/profile/edit") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
            body = data
        }
    }
}