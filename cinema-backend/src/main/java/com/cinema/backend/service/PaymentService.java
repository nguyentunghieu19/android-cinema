package com.cinema.backend.service;

import com.cinema.backend.dto.request.CreatePaymentRequest;
import com.cinema.backend.dto.response.PaymentUrlResponse;
import java.util.Map;
public interface PaymentService {

    PaymentUrlResponse createVNPayPayment(
            CreatePaymentRequest request,
            String username
    );
    String handleVNPayReturn(
            Map<String, String> params
    );
}