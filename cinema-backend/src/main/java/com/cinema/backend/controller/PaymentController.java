package com.cinema.backend.controller;

import com.cinema.backend.dto.request.CreatePaymentRequest;
import com.cinema.backend.dto.response.PaymentUrlResponse;
import com.cinema.backend.service.PaymentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/vnpay")
    public PaymentUrlResponse createVNPayPayment(
            @RequestBody CreatePaymentRequest request,
            Authentication authentication
    ) {

        return paymentService.createVNPayPayment(
                request,
                authentication.getName()
        );

    }
    @GetMapping("/vnpay-return")
    public String vnpayReturn(

            @RequestParam Map<String, String> params

    ) {

        return paymentService.handleVNPayReturn(params);

    }
}