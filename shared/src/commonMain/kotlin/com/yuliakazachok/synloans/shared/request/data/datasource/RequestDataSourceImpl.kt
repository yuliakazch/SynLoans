package com.yuliakazachok.synloans.shared.request.data.datasource

import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment
import com.yuliakazachok.synloans.util.Config.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class RequestDataSourceImpl(
    private val httpClient: HttpClient,
) : RequestDataSource {

    override suspend fun create(data: CreateRequestInfo, token: String) {
        httpClient.post<Unit>("$BASE_URL/loan/requests/") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
            body = data
        }
    }

    override suspend fun getBorrowRequests(token: String): List<RequestCommon> =
        httpClient.get<List<RequestCommon>>("$BASE_URL/loan/requests/") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun getBankRequests(): BankRequests =
        httpClient.get<BankRequests>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
        }

    override suspend fun getRequestDetail(id: Int): RequestCommon =
        httpClient.get<RequestCommon>("$BASE_URL/$id") { // TODO add url
            contentType(ContentType.Application.Json)
        }

    override suspend fun join(data: JoinSyndicateInfo) {
        httpClient.post<Unit>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
            body = data
        }
    }

    override suspend fun getActualSchedule(id: Int): List<Payment> =
        httpClient.get<List<Payment>>("$BASE_URL/$id") { // TODO add url
            contentType(ContentType.Application.Json)
        }

    override suspend fun getPlannedSchedule(id: Int): List<Payment> =
        httpClient.get<List<Payment>>("$BASE_URL/$id") { // TODO add url
            contentType(ContentType.Application.Json)
        }

    override suspend fun cancel(id: Int) {
        httpClient.delete<Unit>("$BASE_URL/") { // TODO add url
            contentType(ContentType.Application.Json)
        }
    }
}