package com.cinema.android.data.remote.api

import com.cinema.android.data.remote.dto.CreatePaymentRequestDto
import com.cinema.android.data.remote.dto.PaymentUrlResponseDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface PaymentApi {

    @POST("api/payments/vnpay")
    suspend fun createVNPayPayment(@Body request: CreatePaymentRequestDto): PaymentUrlResponseDto

    @GET("api/payments/vnpay-return")
    suspend fun confirmVNPayReturn(@QueryMap params: Map<String, String>): ResponseBody
}