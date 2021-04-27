package com.example.upgrade.service;

import com.example.upgrade.entity.Reservation;
import com.example.upgrade.entity.Status;
import com.example.upgrade.exception.BadRequestException;
import com.example.upgrade.exception.ResourceNotFoundException;
import com.example.upgrade.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;

@Service
public class ReservationService {

    private static final String RESERVATION_NOT_FOUND = "Reservation not found for ";

    @Autowired
    private ReservationRepository reservationRepository;

    @Transactional(NOT_SUPPORTED)
    public Reservation getReservation(Long id) throws ResourceNotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        }
        throw new ResourceNotFoundException(RESERVATION_NOT_FOUND + id);
    }

    @Transactional
    public Reservation createReservation(Reservation reservation) throws BadRequestException {
        validateReservationDates(reservation);
        reservation.setStatus(Status.BOOKED);
        return reservationRepository.save(reservation);
    }

    private void validateReservationDates(Reservation reservation) throws BadRequestException {
        if (reservation.getCheckoutDate().isAfter(reservation.getCheckinDate().plusDays(3))) {
            throw new BadRequestException("Only 3 days reservation allowed");
        }
        if (reservation.getCheckinDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new BadRequestException("Same day reservation not allowed");
        }
        if (reservation.getCheckinDate().isAfter(LocalDate.now().plusDays(31))) {
            throw new BadRequestException("Reservation allowed only 30 days in advance");
        }
        List<LocalDate> availability = getAvailability();
        LocalDate checkinDate = reservation.getCheckinDate();
        while (checkinDate.isBefore(reservation.getCheckoutDate())){
            if(!availability.contains(checkinDate)){
                throw new BadRequestException("");
            }
            checkinDate = checkinDate.plusDays(1);
        }
    }

    @Transactional
    public void deleteReservation(Long id) throws ResourceNotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            reservationRepository.deleteById(id);
            return;
        }
        throw new ResourceNotFoundException(RESERVATION_NOT_FOUND + id);
    }

    @Transactional
    public Reservation updateReservation(Reservation reservation) throws ResourceNotFoundException, BadRequestException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservation.getId());
        if (optionalReservation.isPresent()) {
            Reservation existingReservation = optionalReservation.get();
            existingReservation.setCheckinDate(reservation.getCheckinDate());
            existingReservation.setCheckoutDate(reservation.getCheckoutDate());
            validateReservationDates(reservation);
            return reservationRepository.save(existingReservation);
        }
        throw new ResourceNotFoundException(RESERVATION_NOT_FOUND + reservation.getId());
    }

    @Transactional
    public Reservation cancelReservation(Long id) throws ResourceNotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            Reservation existingReservation = optionalReservation.get();
            existingReservation.setStatus(Status.CANCELED);
            return reservationRepository.save(existingReservation);
        }
        throw new ResourceNotFoundException(RESERVATION_NOT_FOUND + id);
    }

    public List<LocalDate> getAvailability() {
        List<Reservation> dateRange = reservationRepository.findByDateRange(LocalDate.now().plusDays(1), LocalDate.now().plusDays(31));

        List<LocalDate> result = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            result.add(LocalDate.now().plusDays(i));
        }

        for (Reservation r : dateRange) {
            LocalDate ld = r.getCheckinDate();
            while (ld.isBefore(r.getCheckoutDate())) {
                result.remove(ld);
                ld = ld.plusDays(1);
            }
        }
        return result;
    }
}
