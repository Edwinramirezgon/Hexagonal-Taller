package com.demo.reservations.infrastructure.config;

import com.demo.reservations.application.port.out.ReservationRepository;
import com.demo.reservations.infrastructure.adapter.out.H2ReservationAdapter;
import com.demo.reservations.infrastructure.adapter.out.persistence.JpaReservationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceConfig {

    @Bean
    public ReservationRepository reservationRepository(JpaReservationRepository jpa) {
        return new H2ReservationAdapter(jpa);
    }
}
