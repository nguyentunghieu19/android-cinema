package com.cinema.android.domain.repository

interface PaymentRepository {

    suspend fun createVNPayPayment(bookingId: Int): Result<String>

    suspend fun confirmVNPayReturn(params: Map<String, String>): Result<Unit>
}