package com.example.upgrade.controller;

import com.example.upgrade.entity.Reservation;
import com.example.upgrade.exception.BadRequestException;
import com.example.upgrade.exception.ResourceNotFoundException;
import com.example.upgrade.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    @GetMapping("/{id}")
    public @ResponseBody
    Reservation getReservation(@PathVariable String id) throws ResourceNotFoundException {
        return reservationService.getReservation(Long.valueOf(id));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public @ResponseBody
    Reservation createReservation(@RequestBody Reservation reservation) throws BadRequestException {
        return reservationService.createReservation(reservation);
    }

    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable String id) throws ResourceNotFoundException {
        reservationService.deleteReservation(Long.valueOf(id));
    }

    @PostMapping("/{id}/cancel")
    public Reservation cancelReservation(@PathVariable String id) throws ResourceNotFoundException {
        return reservationService.cancelReservation(Long.valueOf(id));
    }

    @PutMapping("/{id}")
    public @ResponseBody
    Reservation updateReservation(@PathVariable String id, @RequestBody Reservation reservation)
            throws ResourceNotFoundException, BadRequestException {
        reservation.setId(Long.valueOf(id));
        return reservationService.updateReservation(reservation);
    }
}
