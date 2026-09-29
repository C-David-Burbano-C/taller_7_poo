/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import notificacion.NotificadorService;
import politicas.PoliticaDescuento;
import repositorio.ReservaRepository;

/**
 *
 * @author daferarte
 */
public class ConfirmacionReservaService {
    // Dependencias abstractas (DIP)
    private final ReservaRepository repository;
    private final NotificadorService notificador;
    
    // Inyección de Dependencias por Constructor
    public ConfirmacionReservaService(ReservaRepository repository, NotificadorService notificador) {
        if (repository == null || notificador == null) {
            throw new IllegalArgumentException("Las dependencias no pueden ser nulas");
        }
        this.repository = repository;
        this.notificador = notificador;
    }
    
    // SRP: Coordina el caso de uso sin implementar detalles técnicos
    public double procesar(Reserva reserva, PoliticaDescuento politicaDescuento, double precioBase) {
        // 1. Ejecutar regla de negocio de la entidad de dominio
        reserva.confirmar();

        // 2. Calcular monto total según la política inyectada (OCP)
        double precioFinal = politicaDescuento.aplicarDescuento(precioBase);

        // 3. Persistir en la abstracción (DIP)
        repository.guardar(reserva);

        // 4. Notificar a través de la abstracción (ISP/LSP)
        notificador.notificar(
            reserva.getCliente(),
            "Su reserva ha sido confirmada. Monto final: $" + precioFinal
        );

        return precioFinal;
    }
}
