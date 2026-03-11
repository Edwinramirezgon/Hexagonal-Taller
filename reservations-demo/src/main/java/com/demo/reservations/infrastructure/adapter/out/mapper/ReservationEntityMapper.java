package com.demo.reservations.infrastructure.adapter.out.mapper;

import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.out.persistence.ReservationEntity;


public class ReservationEntityMapper {

    public static ReservationEntity toEntity(Reservation reservation) {
        ReservationEntity entity = new ReservationEntity(
                reservation.getRoomId(),
                reservation.getAttendee(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        if (reservation.getId() != null) {
            entity.setId(reservation.getId());
        }
        return entity;
    }


    public static Reservation toDomain(ReservationEntity entity) {
        Reservation reservation = Reservation.create(
                entity.getRoomId(),
                entity.getAttendee(),
                entity.getStartTime(),
                entity.getEndTime()
        );

        reservation.setId(entity.getId());
        return reservation;
    }
}
