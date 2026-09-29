package com.mycompany.sistema.reservas.dominio;

import Creacionales.builder.ReservaBuilder;
import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import comportamentales.observer.AuditoriaObserver;
import comportamentales.observer.EmailObserver;
import comportamentales.observer.GestorEventosReserva;
import comportamentales.strategy.CancelacionEstricta;
import comportamentales.strategy.CancelacionFlexible;
import comportamentales.strategy.CancelacionModerada;
import comportamentales.strategy.EstrategiaCancelacion;
import java.time.LocalDateTime;

/**
 * Demostración de los conceptos de la Semana 4:
 * 1. Patrones Comportamentales: State, Strategy y Observer.
 * 2. Eliminación de estructuras condicionales anidadas (if-else / switch) mediante polimorfismo.
 * 3. Implementación del patrón Strategy para la evaluación dinámica de reglas de negocio cambiantes.
 */
public class MainSemana4 {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("   SEMANA 4: PATRONES COMPORTAMENTALES Y REGLAS DE NEGOCIO DINÁMICAS");
        System.out.println("================================================================================\n");

        // 1. Inicialización de Entidades
        Cliente cliente = new Cliente("Valentina Duque", new Email("valentina.duque@empresa.com"));
        Habitacion suitePresidencial = new Habitacion(new NumeroHabitacion("P10-101"), 4, 300.0);

        Reserva reserva = new ReservaBuilder()
                .paraCliente(cliente)
                .enHabitacion(suitePresidencial)
                .desde(LocalDateTime.now().plusDays(2))
                .hasta(LocalDateTime.now().plusDays(7)) // 5 noches * $300 = $1500
                .build();

        // 2. Configuración del Patrón Observer
        GestorEventosReserva gestorEventos = new GestorEventosReserva();
        gestorEventos.suscribir(new AuditoriaObserver());
        gestorEventos.suscribir(new EmailObserver());
        reserva.setGestorEventos(gestorEventos);

        System.out.println("--- 1. PATRÓN STATE (Cero IFs en el cliente) ---");
        System.out.println("Estado inicial de la reserva: " + reserva.getEstado());
        System.out.println("Estado inicial de la habitación: " + suitePresidencial.getEstado());

        System.out.println("\n>> Confirmando reserva...");
        reserva.confirmar(); // Transición polimórfica: Pendiente -> Confirmada
        System.out.println("Estado actual de la reserva: " + reserva.getEstado());
        System.out.println("Estado de la habitación tras confirmar: " + suitePresidencial.getEstado());

        // Verificamos protección de estado (Transición inválida)
        try {
            System.out.println("\n>> Intentando confirmar nuevamente una reserva ya confirmada...");
            reserva.confirmar();
        } catch (IllegalStateException e) {
            System.out.println("Excepción capturada por el Estado: " + e.getMessage());
        }

        System.out.println("\n--- 2. PATRÓN STRATEGY (Evaluación Dinámica de Reglas de Negocio) ---");
        System.out.println("Total de la reserva: $" + reserva.getTotal() + " (5 noches x $300)");

        // Demostración de cambio dinámico de estrategias (Polimorfismo en tiempo de ejecución)
        EstrategiaCancelacion flexible = new CancelacionFlexible();
        EstrategiaCancelacion moderada = new CancelacionModerada();
        EstrategiaCancelacion estricta = new CancelacionEstricta();

        int diasAntesDeCheckIn = 2; // Faltan 2 días para el check-in

        System.out.println("\n* Evaluación con Estrategia Flexible:");
        reserva.setEstrategiaCancelacion(flexible);
        System.out.println("Multa calculada dinámicamente: $" + flexible.calcularMulta(reserva, diasAntesDeCheckIn));

        System.out.println("\n* Evaluación con Estrategia Moderada (< 3 días = 25%):");
        reserva.setEstrategiaCancelacion(moderada);
        System.out.println("Multa calculada dinámicamente: $" + moderada.calcularMulta(reserva, diasAntesDeCheckIn));

        System.out.println("\n* Evaluación con Estrategia Estricta (< 7 días = 50%):");
        reserva.setEstrategiaCancelacion(estricta);
        System.out.println("Multa calculada dinámicamente: $" + estricta.calcularMulta(reserva, diasAntesDeCheckIn));

        System.out.println("\n--- 3. PATRÓN OBSERVER Y STATE EN ACCIÓN CONJUNTA ---");
        System.out.println(">> Procediendo a cancelar la reserva confirmada con política Moderada...");
        reserva.setEstrategiaCancelacion(moderada);
        reserva.cancelar(diasAntesDeCheckIn);

        System.out.println("Estado final de la reserva: " + reserva.getEstado());
        System.out.println("Estado final de la habitación (Liberada): " + suitePresidencial.getEstado());

        try {
            System.out.println("\n>> Intentando cancelar una reserva que ya está cancelada...");
            reserva.cancelar(diasAntesDeCheckIn);
        } catch (IllegalStateException e) {
            System.out.println("Excepción capturada por el Estado: " + e.getMessage());
        }

        System.out.println("\n================================================================================");
        System.out.println("   DEMOSTRACIÓN FINALIZADA CON ÉXITO");
        System.out.println("================================================================================");
    }
}
