// CreateVNPayPaymentUseCase.kt
package com.cinema.android.domain.usecase

import com.cinema.android.domain.repository.PaymentRepository
import javax.inject.Inject

class CreateVNPayPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(bookingId: Int): Result<String> {
        return paymentRepository.createVNPayPayment(bookingId)
    }
}