package com.mycompany.hotel.web;

import com.mycompany.hotel.aplicacion.ClienteService;
import com.mycompany.hotel.aplicacion.ComandoCrearHabitacion;
import com.mycompany.hotel.aplicacion.HabitacionService;
import com.mycompany.hotel.aplicacion.ReservaService;
import com.mycompany.hotel.dominio.modelo.Cliente;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.modelo.PoliticaCancelacion;
import com.mycompany.hotel.dominio.modelo.Reserva;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Datos de ejemplo. Activar con: --spring.profiles.active=demo */
@Component
@Profile("demo")
class DatosDemo implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DatosDemo.class);

    private final ClienteService clientes;
    private final HabitacionService habitaciones;
    private final ReservaService reservas;

    DatosDemo(ClienteService clientes, HabitacionService habitaciones, ReservaService reservas) {
        this.clientes = clientes;
        this.habitaciones = habitaciones;
        this.reservas = reservas;
    }

    @Override
    public void run(String... args) {
        Cliente ana = clientes.registrar("Ana Torres", "ana.torres@correo.com");
        Habitacion estandar = habitaciones.crear(
                new ComandoCrearHabitacion.Estandar("P01-101", new BigDecimal("80.00"), 2, 2));
        Habitacion suite = habitaciones.crear(
                new ComandoCrearHabitacion.Suite("P05-501", new BigDecimal("350.00"), 2, true, true));

        LocalDate hoy = LocalDate.now();
        Reserva r1 = reservas.reservar(ana.getId(), estandar.getId(), hoy.plusDays(10), hoy.plusDays(13),
                PoliticaCancelacion.FLEXIBLE);
        reservas.confirmar(ana.getId(), r1.getId());
        Reserva r2 = reservas.reservar(ana.getId(), suite.getId(), hoy.plusDays(30), hoy.plusDays(32),
                PoliticaCancelacion.ESTRICTA);
        reservas.confirmar(ana.getId(), r2.getId());

        LOG.info("DEMO -> clienteId={}  estandarId={}  suiteId={}", ana.getId(), estandar.getId(), suite.getId());
        LOG.info("DEMO -> GET http://localhost:8080/api/clientes/{}/resumen", ana.getId());
    }
}
