package com.cinema.backend.service.impl;

import com.cinema.backend.dto.response.ComboResponse;
import com.cinema.backend.entity.Combo;
import com.cinema.backend.repository.ComboRepository;
import com.cinema.backend.service.ComboService;
import org.springframework.stereotype.Service;
import com.cinema.backend.repository.BookingRepository;
import com.cinema.backend.repository.BookingComboRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import com.cinema.backend.dto.response.BookingComboResponse;
import com.cinema.backend.enums.BookingStatus;
import com.cinema.backend.dto.request.AddComboRequest;
import com.cinema.backend.entity.Booking;
import com.cinema.backend.entity.BookingCombo;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComboServiceImpl implements ComboService {

    private final ComboRepository comboRepository;
    private final BookingRepository bookingRepository;

    private final BookingComboRepository bookingComboRepository;
    public ComboServiceImpl(
            ComboRepository comboRepository,
            BookingRepository bookingRepository,
            BookingComboRepository bookingComboRepository
    ) {

        this.comboRepository = comboRepository;
        this.bookingRepository = bookingRepository;
        this.bookingComboRepository = bookingComboRepository;

    }

    @Override
    public List<ComboResponse> getAllCombos() {

        List<Combo> combos = comboRepository.findByStatusTrue();

        List<ComboResponse> responses = new ArrayList<>();

        for (Combo combo : combos) {

            ComboResponse response = new ComboResponse();

            response.setId(combo.getId());

            response.setName(combo.getName());

            response.setDescription(combo.getDescription());

            response.setPrice(combo.getPrice());

            response.setImage(combo.getImage());

            responses.add(response);

        }

        return responses;

    }
    @Override
    @Transactional
    public void addComboToBooking(
            Integer bookingId,
            AddComboRequest request
    ) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));

        Combo combo = comboRepository.findById(request.getComboId())
                .orElseThrow(() ->
                        new RuntimeException("Combo not found"));

        if (!booking.getStatus().equals(BookingStatus.PENDING)) {

            throw new RuntimeException("Booking is not pending");

        }

        BookingCombo bookingCombo = bookingComboRepository
                .findByBookingIdAndComboId(
                        bookingId,
                        combo.getId()
                )
                .orElse(null);

        if (bookingCombo == null) {

            bookingCombo = new BookingCombo();

            bookingCombo.setBooking(booking);

            bookingCombo.setCombo(combo);

            bookingCombo.setQuantity(request.getQuantity());

            bookingCombo.setPrice(combo.getPrice());

        } else {

            bookingCombo.setQuantity(
                    bookingCombo.getQuantity() + request.getQuantity()
            );

        }

        bookingComboRepository.save(bookingCombo);

        BigDecimal comboTotal =
                combo.getPrice().multiply(
                        BigDecimal.valueOf(request.getQuantity())
                );

        booking.setTotalAmount(
                booking.getTotalAmount().add(comboTotal)
        );

        bookingRepository.save(booking);

    }
    @Override
    public List<BookingComboResponse> getBookingCombos(Integer bookingId) {

        List<BookingCombo> bookingCombos =
                bookingComboRepository.findByBookingId(bookingId);

        List<BookingComboResponse> responses =
                new ArrayList<>();

        for (BookingCombo bookingCombo : bookingCombos) {

            BookingComboResponse response =
                    new BookingComboResponse();

            response.setComboId(
                    bookingCombo.getCombo().getId()
            );

            response.setComboName(
                    bookingCombo.getCombo().getName()
            );

            response.setQuantity(
                    bookingCombo.getQuantity()
            );

            response.setUnitPrice(
                    bookingCombo.getPrice()
            );

            response.setTotalPrice(
                    bookingCombo.getPrice().multiply(
                            BigDecimal.valueOf(
                                    bookingCombo.getQuantity()
                            )
                    )
            );

            responses.add(response);

        }

        return responses;

    }
    @Override
    @Transactional
    public void removeComboFromBooking(
            Integer bookingId,
            Integer comboId
    ) {

        BookingCombo bookingCombo =
                bookingComboRepository
                        .findByBookingIdAndComboId(
                                bookingId,
                                comboId
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Combo not found"));

        Booking booking = bookingCombo.getBooking();

        BigDecimal subtractAmount =
                bookingCombo.getPrice().multiply(
                        BigDecimal.valueOf(
                                bookingCombo.getQuantity()
                        )
                );

        booking.setTotalAmount(
                booking.getTotalAmount().subtract(subtractAmount)
        );

        bookingRepository.save(booking);

        bookingComboRepository.delete(bookingCombo);

    }

}