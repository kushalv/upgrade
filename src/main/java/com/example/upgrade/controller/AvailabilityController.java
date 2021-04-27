package com.example.upgrade.controller;

import com.example.upgrade.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {

    @Autowired
    private ReservationService reservationService;


    @GetMapping()
    public @ResponseBody
    List<LocalDate> getAvailability(){
        return reservationService.getAvailability();
    }
}
