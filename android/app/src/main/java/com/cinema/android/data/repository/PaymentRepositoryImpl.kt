package com.cinema.android.data.repository

import com.cinema.android.data.remote.api.PaymentApi
import com.cinema.android.data.remote.dto.CreatePaymentRequestDto
import com.cinema.android.domain.repository.PaymentRepository
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentApi: PaymentApi
) : PaymentRepository {

    override suspend fun createVNPayPayment(bookingId: Int): Result<String> {
        return try {
            val response = paymentApi.createVNPayPayment(CreatePaymentRequestDto(bookingId))
            Result.success(response.paymentUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun confirmVNPayReturn(params: Map<String, String>): Result<Unit> {
        return try {
            paymentApi.confirmVNPayReturn(params)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}