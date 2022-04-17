package com.yuliakazachok.synloans.shared.request.domain.repository

import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.Payment
import com.yuliakazachok.synloans.shared.request.domain.entity.payment.PaymentInfo

interface RequestRepository {

    suspend fun create(data: CreateRequestInfo)

    suspend fun getBorrowRequests(): List<RequestCommon>

    suspend fun getBankRequests(): BankRequests

    suspend fun getRequestDetail(id: Int): RequestCommon

    suspend fun join(data: JoinSyndicateInfo)

    suspend fun getActualSchedule(id: Int): List<Payment>

    suspend fun getPlannedSchedule(id: Int): List<PaymentInfo>

    suspend fun cancel(id: Int)

    suspend fun makePayment(id: Int, payment: Payment)
}