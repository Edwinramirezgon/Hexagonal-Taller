package com.demo.reservations.infrastructure.adapter.out;

import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.out.mapper.ReservationEntityMapper;
import com.demo.reservations.infrastructure.adapter.out.persistence.JpaReservationRepository;
import com.demo.reservations.infrastructure.adapter.out.persistence.ReservationEntity;

import java.util.List;
import java.util.Optional;

public class H2ReservationAdapter implements ReservationRepository {

    private final JpaReservationRepository jpa;

    public H2ReservationAdapter(JpaReservationRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Reservation save(Reservation reservation) {

        ReservationEntity entity = ReservationEntityMapper.toEntity(reservation);

        ReservationEntity saved = jpa.save(entity);
      
        return ReservationEntityMapper.toDomain(saved);
    }

    @Override
    public List<Reservation> findAll() {
        return jpa.findAll()
                .stream()
                .map(ReservationEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return jpa.findById(id)
                .map(ReservationEntityMapper::toDomain);
    }

    @Override
    public List<Reservation> findByRoomId(String roomId) {
        return jpa.findByRoomId(roomId)
                .stream()
                .map(ReservationEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}
