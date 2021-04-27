package com.example.upgrade.repository;

import com.example.upgrade.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Query("select r from Reservation r where r.checkinDate > ?1 and r.checkoutDate < ?2")
    List<Reservation> findByDateRange(LocalDate checkIn, LocalDate checkOut);
}
