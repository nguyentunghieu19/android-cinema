package com.cinema.backend.service.impl;

import com.cinema.backend.entity.Ticket;
import com.cinema.backend.util.VNPayUtil;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import com.cinema.backend.config.VNPayConfig;
import com.cinema.backend.dto.request.CreatePaymentRequest;
import com.cinema.backend.dto.response.PaymentUrlResponse;
import com.cinema.backend.entity.Booking;
import com.cinema.backend.entity.Payment;
import com.cinema.backend.entity.User;
import com.cinema.backend.enums.BookingStatus;
import com.cinema.backend.enums.PaymentMethod;
import com.cinema.backend.enums.PaymentStatus;
import com.cinema.backend.enums.TicketStatus;
import com.cinema.backend.repository.BookingRepository;
import com.cinema.backend.repository.PaymentRepository;
import com.cinema.backend.repository.UserRepository;
import com.cinema.backend.service.PaymentService;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.cinema.backend.repository.TicketRepository;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final VNPayConfig vnPayConfig;
    private final TicketRepository ticketRepository;
    public PaymentServiceImpl(
            BookingRepository bookingRepository,
            PaymentRepository paymentRepository,
            UserRepository userRepository,
            VNPayConfig vnPayConfig,TicketRepository ticketRepository
    ) {

        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.vnPayConfig = vnPayConfig;
        this.ticketRepository=ticketRepository;
    }

    @Override
    public PaymentUrlResponse createVNPayPayment(
            CreatePaymentRequest request,
            String username
    ) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        if (!booking.getUser().getId().equals(user.getId())) {

            throw new RuntimeException("Access denied");

        }

        if (booking.getStatus() != BookingStatus.PENDING) {

            throw new RuntimeException("Booking is not pending");

        }
        Payment payment = paymentRepository
                .findByBookingId(booking.getId())
                .orElse(null);

        if (payment == null) {

            payment = new Payment();

            payment.setBooking(booking);

            payment.setAmount(booking.getTotalAmount());

            payment.setPaymentMethod(PaymentMethod.VNPAY);

            payment.setStatus(PaymentStatus.PENDING);

            payment.setPaymentTime(LocalDateTime.now());

            paymentRepository.save(payment);

        }
        String txnRef = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));

        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyyMMddHHmmss");

        String createDate = formatter.format(calendar.getTime());

        calendar.add(Calendar.MINUTE, 15);

        String expireDate = formatter.format(calendar.getTime());

        Map<String, String> vnpParams = new HashMap<>();

        vnpParams.put("vnp_Version", vnPayConfig.getVersion());

        vnpParams.put("vnp_Command", vnPayConfig.getCommand());

        vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());

        vnpParams.put("vnp_CurrCode", vnPayConfig.getCurrCode());

        vnpParams.put("vnp_Locale", vnPayConfig.getLocale());

        vnpParams.put("vnp_TxnRef", txnRef);

        vnpParams.put(
                "vnp_OrderInfo",
                "Thanh toan don dat ve " + booking.getBookingCode()
        );

        vnpParams.put(
                "vnp_OrderType",
                "other"
        );

        vnpParams.put(
                "vnp_Amount",
                booking.getTotalAmount()
                        .multiply(BigDecimal.valueOf(100))
                        .toBigInteger()
                        .toString()
        );

        vnpParams.put(
                "vnp_ReturnUrl",
                vnPayConfig.getReturnUrl()
        );

        vnpParams.put(
                "vnp_CreateDate",
                createDate
        );

        vnpParams.put(
                "vnp_ExpireDate",
                expireDate
        );

        vnpParams.put(
                "vnp_IpAddr",
                "127.0.0.1"
        );
        String query = VNPayUtil.buildQuery(vnpParams);

        String secureHash =
                VNPayUtil.hmacSHA512(
                        vnPayConfig.getHashSecret(),
                        query
                );
        String paymentUrl =
                vnPayConfig.getPayUrl()
                        + "?"
                        + query
                        + "&vnp_SecureHash="
                        + secureHash;
        payment.setTransactionId(txnRef);

        paymentRepository.save(payment);

        PaymentUrlResponse response = new PaymentUrlResponse();

        response.setPaymentUrl(paymentUrl);

        return response;
        
    }

    @Override
    @Transactional
    public String handleVNPayReturn(Map<String, String> params) {

        String vnpSecureHash = params.get("vnp_SecureHash");

        String txnRef = params.get("vnp_TxnRef");

        String responseCode = params.get("vnp_ResponseCode");

        if (txnRef == null || responseCode == null || vnpSecureHash == null) {

            throw new RuntimeException("Invalid VNPay response");

        }

        // ===== Xác minh chữ ký =====

        Map<String, String> hashParams = new HashMap<>(params);

        hashParams.remove("vnp_SecureHash");

        hashParams.remove("vnp_SecureHashType");

        String signData = VNPayUtil.buildQuery(hashParams);

        String secureHash = VNPayUtil.hmacSHA512(
                vnPayConfig.getHashSecret(),
                signData
        );

        if (!secureHash.equalsIgnoreCase(vnpSecureHash)) {

            throw new RuntimeException("Invalid VNPay SecureHash");

        }

        // ===========================

        Payment payment = paymentRepository
                .findByTransactionId(txnRef)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        Booking booking = payment.getBooking();

        List<Ticket> tickets =
                ticketRepository.findByBookingId(booking.getId());

        if ("00".equals(responseCode)) {

            payment.setStatus(PaymentStatus.SUCCESS);

            booking.setStatus(BookingStatus.PAID);

            for (Ticket ticket : tickets) {

                ticket.setStatus(TicketStatus.BOOKED);

            }

        } else {

            payment.setStatus(PaymentStatus.FAILED);

            booking.setStatus(BookingStatus.CANCELLED);

            for (Ticket ticket : tickets) {

                ticket.setStatus(TicketStatus.CANCELLED);

            }

        }

        paymentRepository.save(payment);

        bookingRepository.save(booking);

        ticketRepository.saveAll(tickets);

        return "Thanh toán xử lý thành công";

    }

}