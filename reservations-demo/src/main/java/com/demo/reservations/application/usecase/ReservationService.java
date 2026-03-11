package com.demo.reservations.application.usecase;

import com.demo.reservations.application.port.in.ReservationUseCase;
import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.domain.exception.RoomNotAvailableException;
import com.demo.reservations.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReservationService implements ReservationUseCase {


    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }


    @Override
    public Reservation create(Reservation reservation) {
        List<Reservation> existingReservations = repository.findByRoomId(reservation.getRoomId());

        boolean hasConflict = existingReservations.stream()
                .anyMatch(existing -> existing.overlaps(
                        reservation.getStartTime(),
                        reservation.getEndTime()
                ));

        if (hasConflict) {
            throw new RoomNotAvailableException(
                    reservation.getRoomId(),
                    reservation.getStartTime().toString(),
                    reservation.getEndTime().toString()
            );
        }

        return repository.save(reservation);
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
    }

    @Override
    public Reservation update(Long id, Reservation updated) {
        Reservation existing = findById(id);

        List<Reservation> otherReservations = repository.findByRoomId(updated.getRoomId())
                .stream()
                .filter(r -> !r.getId().equals(id)) 
                .toList();

        boolean hasConflict = otherReservations.stream()
                .anyMatch(r -> r.overlaps(updated.getStartTime(), updated.getEndTime()));

        if (hasConflict) {
            throw new RoomNotAvailableException(
                    updated.getRoomId(),
                    updated.getStartTime().toString(),
                    updated.getEndTime().toString()
            );
        }

        existing.setRoomId(updated.getRoomId());
        existing.setAttendee(updated.getAttendee());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getEndTime());

        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        findById(id); 
        repository.deleteById(id);
    }
}
