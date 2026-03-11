package com.demo.reservations.infrastructure.adapter.in;

import com.demo.reservations.application.port.in.ReservationUseCase;
import com.demo.reservations.domain.exception.RoomNotAvailableException;
import com.demo.reservations.domain.model.Reservation;
import com.demo.reservations.infrastructure.adapter.in.dto.ReservationRequest;
import com.demo.reservations.infrastructure.adapter.in.dto.ReservationResponse;
import com.demo.reservations.infrastructure.adapter.in.mapper.ReservationRequestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationUseCase useCase;

    public ReservationController(ReservationUseCase useCase) {
        this.useCase = useCase;
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationRequest request) {
        try {
            Reservation domain    = ReservationRequestMapper.toDomain(request);
            Reservation saved     = useCase.create(domain);
            ReservationResponse response = ReservationRequestMapper.toResponse(saved);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RoomNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        List<ReservationResponse> response = useCase.findAll()
                .stream()
                .map(ReservationRequestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            ReservationResponse response = ReservationRequestMapper.toResponse(useCase.findById(id));
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody ReservationRequest request) {
        try {
            Reservation domain   = ReservationRequestMapper.toDomain(request);
            Reservation updated  = useCase.update(id, domain);
            return ResponseEntity.ok(ReservationRequestMapper.toResponse(updated));

        } catch (RoomNotAvailableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            useCase.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
