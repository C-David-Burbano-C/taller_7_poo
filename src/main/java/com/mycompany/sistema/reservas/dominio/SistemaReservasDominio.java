/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sistema.reservas.dominio;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.time.LocalDateTime;

/**
 *
 * @author daferarte
 */
public class SistemaReservasDominio {

    public static void main(String[] args) {
        try {
            Email email = new Email("juan.perez@empresa.com");
            Cliente cliente = new Cliente("Juan Pérez", email);
            System.out.println("Cliente creado: " + cliente.getNombre() + " (Activo: " + cliente.isActivo() + ")");
            
            Habitacion habitacion = new Habitacion(new NumeroHabitacion("P01-101"), 2, 150.0);
            LocalDateTime inicio = LocalDateTime.now().plusDays(1);
            LocalDateTime fin = LocalDateTime.now().plusDays(3);
            RangoFechas periodo = new RangoFechas(inicio, fin);

            Reserva reserva = new Reserva(cliente, habitacion, periodo);
            System.out.println("Reserva creada con ID: " + reserva.getId() + " - Estado: " + reserva.getEstado());

            reserva.confirmar();
            System.out.println("Estado tras confirmar: " + reserva.getEstado() + " | Habitación: " + habitacion.getEstado());

            reserva.cancelar();
            System.out.println("Estado tras cancelar: " + reserva.getEstado() + " | Habitación: " + habitacion.getEstado());

            reserva.confirmar(); // Esto lanzará IllegalStateException porque está cancelada

        } catch (Exception e) {
            System.err.println("ERROR capturado (comportamiento esperado): " + e.getMessage());
        }
    }
}
