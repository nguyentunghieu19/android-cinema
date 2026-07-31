package com.cinema.backend.controller;

import com.cinema.backend.dto.response.ComboResponse;
import com.cinema.backend.service.ComboService;
import com.cinema.backend.dto.request.AddComboRequest;
import com.cinema.backend.dto.response.BookingComboResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@RestController
@RequestMapping("/api/combos")
public class ComboController {

    private final ComboService comboService;

    public ComboController(ComboService comboService) {
        this.comboService = comboService;
    }

    @GetMapping
    public List<ComboResponse> getAllCombos() {

        return comboService.getAllCombos();

    }
    @PostMapping("/booking/{bookingId}")
    public String addComboToBooking(

            @PathVariable Integer bookingId,

            @RequestBody AddComboRequest request

    ) {

        comboService.addComboToBooking(
                bookingId,
                request
        );

        return "Thêm combo thành công";

    }
    @GetMapping("/booking/{bookingId}")
    public List<BookingComboResponse> getBookingCombos(

            @PathVariable Integer bookingId

    ){

        return comboService.getBookingCombos(
                bookingId
        );

    }
    @DeleteMapping("/booking/{bookingId}/{comboId}")
    public String removeCombo(

            @PathVariable Integer bookingId,

            @PathVariable Integer comboId

    ){

        comboService.removeComboFromBooking(
                bookingId,
                comboId
        );

        return "Xóa combo thành công";

    }
}