package com.mycompany.sistema.reservas.dominio;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.time.LocalDateTime;
import notificacion.NotificadorService;
import notificacion.WhatsAppNotificadorService;
import politicas.DescuentoEstadiaLarga;
import politicas.PoliticaDescuento;
import repositorio.ReservaArchivoRepository;
import repositorio.ReservaRepository;
import servicio.ConfirmacionReservaService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
public class mainsemana2 {
    public static void main(String[] args) {
        System.out.println("=== ESCENARIO 1: RESERVA EXITOSA (SOLID + DOMINIO RICO) ===");
        try {
            Cliente cliente = new Cliente("Santiago Ospina", new Email("santiago@empresa.com"));
            NumeroHabitacion numHab = new NumeroHabitacion("P03-302");
            Habitacion habitacion = new Habitacion(numHab, 2);

            // Periodo de 10 días para activar DescuentoEstadiaLarga (> 7 días)
            RangoFechas periodoLargo = new RangoFechas(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(11)
            );

            Reserva reserva = new Reserva(cliente, habitacion, periodoLargo);

            // Inyección de infraestructura desacoplada
            ReservaRepository repoArchivo = new ReservaArchivoRepository("reservas_log.txt");
            NotificadorService notifWhatsApp = new WhatsAppNotificadorService();
            ConfirmacionReservaService servicio = new ConfirmacionReservaService(repoArchivo, notifWhatsApp);

            PoliticaDescuento descuentoEstadia = new DescuentoEstadiaLarga(periodoLargo);
            double total = servicio.procesar(reserva, descuentoEstadia, 1000.0);

            System.out.println("Habitación tras confirmación: " + habitacion.getEstado());
            System.out.println("Precio final con 25% desc: $" + total);

        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n=== ESCENARIO 2: HABITACIÓN EN MANTENIMIENTO (FAIL-FAST) ===");
        try {
            Cliente cliente2 = new Cliente("Valentina Ríos", new Email("valen@empresa.com"));
            Habitacion habMantenimiento = new Habitacion(new NumeroHabitacion("P01-101"), 4);
            habMantenimiento.marcarEnMantenimiento();

            RangoFechas periodo = new RangoFechas(LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(4));

            // Intentar reservar habitación en mantenimiento disparará la excepción
            new Reserva(cliente2, habMantenimiento, periodo);

        } catch (IllegalStateException e) {
            System.out.println("Excepción de Dominio capturada con éxito: " + e.getMessage());
        }
    }
}
