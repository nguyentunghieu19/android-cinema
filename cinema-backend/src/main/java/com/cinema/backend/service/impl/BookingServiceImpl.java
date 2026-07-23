package com.cinema.backend.service.impl;

import com.cinema.backend.dto.request.CreateBookingRequest;
import com.cinema.backend.dto.response.CreateBookingResponse;
import com.cinema.backend.entity.*;
import com.cinema.backend.enums.TicketStatus;
import com.cinema.backend.repository.TicketRepository;
import com.cinema.backend.enums.BookingStatus;
import com.cinema.backend.repository.*;
import com.cinema.backend.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cinema.backend.dto.response.MyBookingResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.cinema.backend.dto.response.BookingDetailResponse;
import com.cinema.backend.enums.PaymentStatus;

import com.cinema.backend.entity.Ticket;

import com.cinema.backend.enums.PaymentMethod;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.util.UUID;
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            TicketRepository ticketRepository,
            PaymentRepository paymentRepository,
            UserRepository userRepository,
            ShowtimeRepository showtimeRepository,
            SeatRepository seatRepository

    ) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;

    }
    @Transactional
    @Override
    public CreateBookingResponse createBooking(
            CreateBookingRequest request,
            String username

    ) {

        // 1. Kiểm tra User
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy người dùng"));

        // 2. Kiểm tra Showtime
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy suất chiếu"));

        // 3. Kiểm tra danh sách ghế
        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new RuntimeException("Bạn chưa chọn ghế");
        }
        List<Seat> seats =
                seatRepository.findAllById(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new RuntimeException("Có ghế không tồn tại");
        }
        for (Seat seat : seats) {

            if (!seat.getRoom().getId().equals(showtime.getRoom().getId())) {

                throw new RuntimeException(
                        "Ghế " +
                                seat.getSeatRow() +
                                seat.getSeatNumber() +
                                " không thuộc phòng chiếu này"
                );

            }

        }
        for (Seat seat : seats) {

            boolean booked = ticketRepository
                    .existsByShowtimeIdAndSeatIdAndStatus(
                            showtime.getId(),
                            seat.getId(),
                            TicketStatus.PENDING
                    );

            if (booked) {

                throw new RuntimeException(
                        "Ghế "
                                + seat.getSeatRow()
                                + seat.getSeatNumber()
                                + " đang được người khác giữ."
                );

            }

            booked = ticketRepository
                    .existsByShowtimeIdAndSeatIdAndStatus(
                            showtime.getId(),
                            seat.getId(),
                            TicketStatus.BOOKED
                    );

            if (booked) {

                throw new RuntimeException(
                        "Ghế "
                                + seat.getSeatRow()
                                + seat.getSeatNumber()
                                + " đã được bán."
                );

            }

        }
        // 4. Tạm thời trả dữ liệu test
        String bookingCode = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
        CreateBookingResponse response = new CreateBookingResponse();


        BigDecimal totalAmount = showtime.getPrice()
                .multiply(BigDecimal.valueOf(request.getSeatIds().size()));
        Booking booking = new Booking();

        booking.setBookingCode(bookingCode);

        booking.setUser(user);

        booking.setShowtime(showtime);

        booking.setBookingTime(LocalDateTime.now());

        booking.setTotalAmount(totalAmount);

        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);
        //tạo ticket
        for (Seat seat : seats) {

            Ticket ticket = new Ticket();

            ticket.setBooking(booking);
            ticket.setShowtime(showtime);
            ticket.setSeat(seat);

            ticket.setPrice(showtime.getPrice());

            ticket.setStatus(TicketStatus.PENDING);

            ticket.setTicketCode(
                    "TK" +
                            UUID.randomUUID()
                                    .toString()
                                    .replace("-", "")
                                    .substring(0,10)
                                    .toUpperCase()
            );

            ticketRepository.save(ticket);

        }
        Payment payment = new Payment();

        payment.setBooking(booking);

        payment.setAmount(totalAmount);

        payment.setPaymentMethod(
                PaymentMethod.valueOf(request.getPaymentMethod())
        );

        payment.setStatus(PaymentStatus.PENDING);

        paymentRepository.save(payment);
        response.setBookingId(booking.getId());

        response.setBookingCode(booking.getBookingCode());

        response.setTotalAmount(booking.getTotalAmount());
        return response;
    }
    @Override
    public List<MyBookingResponse> getMyBookings() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        List<Booking> bookings =
                bookingRepository.findByUserUsernameOrderByBookingTimeDesc(username);

        List<MyBookingResponse> responses = new ArrayList<>();

        for (Booking booking : bookings) {

            MyBookingResponse response = new MyBookingResponse();

            response.setBookingId(booking.getId());
            response.setBookingCode(booking.getBookingCode());

            response.setMovieName(
                    booking.getShowtime()
                            .getMovie()
                            .getTitle()
            );

            response.setCinemaName(
                    booking.getShowtime()
                            .getRoom()
                            .getCinema()
                            .getName()
            );

            response.setRoomName(
                    booking.getShowtime()
                            .getRoom()
                            .getName()
            );

            response.setShowtime(
                    booking.getShowtime()
                            .getStartTime()
            );

            response.setTotalAmount(
                    booking.getTotalAmount()
            );

            response.setStatus(
                    booking.getStatus().name()
            );

            responses.add(response);
        }

        return responses;
    }
    @Override
    public BookingDetailResponse getBookingDetail(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        boolean isStaffOrAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN")
                                || a.getAuthority().equals("ROLE_STAFF"));

        if (!booking.getUser().getUsername().equals(username)
                && !isStaffOrAdmin) {

            throw new RuntimeException("Access denied");
        }

        List<Ticket> tickets =
                ticketRepository.findByBookingId(bookingId);

        BookingDetailResponse response = new BookingDetailResponse();

        response.setBookingId(booking.getId());

        response.setBookingCode(
                booking.getBookingCode());

        response.setMovieName(
                booking.getShowtime()
                        .getMovie()
                        .getTitle());

        response.setCinemaName(
                booking.getShowtime()
                        .getRoom()
                        .getCinema()
                        .getName());

        response.setRoomName(
                booking.getShowtime()
                        .getRoom()
                        .getName());

        response.setShowtime(
                booking.getShowtime()
                        .getStartTime());

        response.setTotalAmount(
                booking.getTotalAmount());

        response.setBookingStatus(
                booking.getStatus().name());

        List<String> seatNames = new ArrayList<>();

        for (Ticket ticket : tickets) {

            seatNames.add(
                    ticket.getSeat().getSeatRow()
                            + String.valueOf(ticket.getSeat().getSeatNumber())
            );
        }

        response.setSeats(seatNames);

        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElse(null);

        if (payment != null) {

            response.setPaymentStatus(
                    payment.getStatus().name());

            response.setPaymentMethod(
                    payment.getPaymentMethod().name());
        }

        return response;
    }
    @Override
    public void cancelBooking(Integer bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        boolean isStaffOrAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN")
                                || a.getAuthority().equals("ROLE_STAFF"));

        if (!booking.getUser().getUsername().equals(username)
                && !isStaffOrAdmin) {

            throw new RuntimeException("Access denied");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("Booking cannot be cancelled");
        }

        if (booking.getBookingTime()
                .plusMinutes(15)
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Booking expired");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

        Payment payment = paymentRepository
                .findByBookingId(bookingId)
                .orElse(null);

        if (payment != null) {

            payment.setStatus(PaymentStatus.CANCELLED);

            paymentRepository.save(payment);
        }

        List<Ticket> tickets =
                ticketRepository.findByBookingId(bookingId);

        for (Ticket ticket : tickets) {

            ticket.setStatus(TicketStatus.CANCELLED);
        }

        ticketRepository.saveAll(tickets);
    }
    @Override
    public void expirePendingBookings() {

        List<Booking> bookings =
                bookingRepository.findByStatus(BookingStatus.PENDING);

        for (Booking booking : bookings) {

            if (booking.getBookingTime()
                    .plusMinutes(15)
                    .isBefore(LocalDateTime.now())) {

                booking.setStatus(BookingStatus.EXPIRED);
                bookingRepository.save(booking);

                paymentRepository.findByBookingId(booking.getId())
                        .ifPresent(payment -> {

                            payment.setStatus(PaymentStatus.CANCELLED);

                            paymentRepository.save(payment);

                        });

                List<Ticket> tickets =
                        ticketRepository.findByBookingId(booking.getId());

                for (Ticket ticket : tickets) {

                    ticket.setStatus(TicketStatus.CANCELLED);

                    ticketRepository.save(ticket);

                }

                System.out.println(
                        "Expired booking: "
                                + booking.getBookingCode()
                );

            }

        }

    }
}