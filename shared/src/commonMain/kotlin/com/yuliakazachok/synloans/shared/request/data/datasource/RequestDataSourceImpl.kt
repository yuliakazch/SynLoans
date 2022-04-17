package com.yuliakazachok.synloans.shared.request.data.datasource

import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo
import com.yuliakazachok.synloans.util.Config.BASE_URL
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class RequestDataSourceImpl(
    private val httpClient: HttpClient,
) : RequestDataSource {

    override suspend fun create(data: CreateRequestInfo, token: String) {
        httpClient.post<Unit>("$BASE_URL/loans/requests/") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
            body = data
        }
    }

    override suspend fun getBorrowRequests(token: String): List<RequestCommon> =
        httpClient.get<List<RequestCommon>>("$BASE_URL/loans/requests/") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun getBankRequests(token: String): BankRequests =
        httpClient.get<BankRequests>("$BASE_URL/loans/requests/all") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun getRequestDetail(id: Int, token: String): RequestCommon =
        httpClient.get<RequestCommon>("$BASE_URL/loans/requests/$id") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun join(data: JoinSyndicateInfo, token: String) {
        httpClient.post<Unit>("$BASE_URL/syndicates/join") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
            body = data
        }
    }

    override suspend fun exit(id: Int, token: String) {
        httpClient.delete<Unit>("$BASE_URL/syndicates/$id") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }
    }

    override suspend fun getActualSchedule(id: Int, token: String): List<Payment> =
        httpClient.get<List<Payment>>("$BASE_URL/loans/$id/payments/actual") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun getPlannedSchedule(id: Int, token: String): List<PaymentInfo> =
        httpClient.get<List<PaymentInfo>>("$BASE_URL/loans/$id/payments/plan") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }

    override suspend fun cancel(id: Int, token: String) {
        httpClient.delete<Unit>("$BASE_URL/loans/requests/$id") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
        }
    }

    override suspend fun makePayment(id: Int, data: Payment, token: String) {
        httpClient.post<Unit>("$BASE_URL/loans/$id/pay") {
            contentType(ContentType.Application.Json)
            header(HttpHeaders.Authorization, token)
            body = data
        }
    }
}