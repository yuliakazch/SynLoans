package com.yuliakazachok.synloans.shared.request.domain.repository

import com.yuliakazachok.synloans.shared.request.domain.entity.list.BankRequests
import com.yuliakazachok.synloans.shared.request.domain.entity.list.BorrowRequest
import com.yuliakazachok.synloans.shared.request.domain.entity.create.CreateRequestInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.detail.RequestCommon
import com.yuliakazachok.synloans.shared.request.domain.entity.join.JoinSyndicateInfo
import com.yuliakazachok.synloans.shared.request.domain.entity.schedule.Payment

interface RequestRepository {

    suspend fun create(data: CreateRequestInfo)

    suspend fun getBorrowRequests(): List<BorrowRequest>

    suspend fun getBankRequests(): BankRequests

    suspend fun getRequestDetail(id: Int): RequestCommon

    suspend fun join(data: JoinSyndicateInfo)

    suspend fun getActualSchedule(id: Int): List<Payment>

    suspend fun getPlannedSchedule(id: Int): List<Payment>

    suspend fun cancel(id: Int)
}