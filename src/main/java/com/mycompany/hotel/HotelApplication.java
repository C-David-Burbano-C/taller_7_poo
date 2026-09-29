package com.mycompany.hotel;

import java.time.Clock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HotelApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotelApplication.class, args);
    }

    /** El reloj se inyecta para que las reglas dependientes de "hoy" sean testeables. */
    @Bean
    Clock reloj() {
        return Clock.systemDefaultZone();
    }
}
