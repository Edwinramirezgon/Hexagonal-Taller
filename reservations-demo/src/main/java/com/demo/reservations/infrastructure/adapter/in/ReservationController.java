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


/**
 * <b>Adaptador de Entrada REST (Driving Adapter)</b> para el sistema de Reservas.
 * Siguiendo la <b>Arquitectura Hexagonal</b>, esta clase cumple el rol de Adaptador Primario.
 * Su responsabilidad es transformar el protocolo de comunicación externo (HTTP/JSON)
 * en una llamada al núcleo de la aplicación mediante el puerto de entrada {@link ReservationUseCase}.
 */
@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    /**
     * Puerto de entrada (Inbound Port).
     * Representa el contrato de lo que el núcleo de la aplicación puede hacer.
     */
    private final ReservationUseCase useCase;

    /**
     * Inyección del puerto mediante constructor.
     * Inyecta el servicio que implementa la lógica, manteniendo el desacoplamiento.
     * * @param useCase Interfaz del puerto de entrada definido en la capa de aplicación.
     */
    public ReservationController(ReservationUseCase useCase) {
        this.useCase = useCase;
    }


    /**
     * Orquestación para la creación de una reserva.
     * <p>
     * <b>Flujo Hexagonal:</b>
     * 1. Transforma el {@link ReservationRequest} (DTO de infraestructura) a {@link Reservation} (Modelo de dominio).
     * 2. Invoca el puerto de entrada para ejecutar la lógica de negocio.
     * 3. Mapea el resultado de dominio a un {@link ReservationResponse} para la respuesta externa.
     * </p>
     * * @param request Datos de entrada recibidos por el cliente REST.
     * @return ResponseEntity con el resultado de la operación y el código de estado HTTP correspondiente.
     */
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


    /**
     * Consulta masiva de reservas.
     * <p>
     * Implementa el flujo de salida transformando la colección de objetos de dominio
     * recuperados por el puerto en una lista de DTOs.
     * </p>
     * * @return Lista de reservas en formato de infraestructura (Response DTO).
     */
    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        List<ReservationResponse> response = useCase.findAll()
                .stream()
                .map(ReservationRequestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }


    /**
     * Consulta individual por identificador.
     * * @param id Identificador técnico de la reserva.
     * @return DTO de la reserva o error 404 si el recurso no es localizado por la capa de aplicación.
     */
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


    /**
     * Actualización de una reserva existente.
     * <p>
     * Este método asegura que cualquier cambio cumpla con las reglas del dominio antes
     * de persistir los datos a través del puerto.
     * </p>
     * * @param id Identificador de la reserva a modificar.
     * @param request Nuevos datos de la reserva.
     * @return Resultado de la actualización mapeado a DTO de respuesta.
     */
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


    /**
     * Eliminación lógica o física de una reserva.
     * * @param id Identificador de la reserva.
     * @return 204 No Content si la operación es exitosa en la capa de aplicación.
     */
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
