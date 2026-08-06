// ConfirmVNPayReturnUseCase.kt
package com.cinema.android.domain.usecase

import com.cinema.android.domain.repository.PaymentRepository
import javax.inject.Inject

class ConfirmVNPayReturnUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(params: Map<String, String>): Result<Unit> {
        return paymentRepository.confirmVNPayReturn(params)
    }
}